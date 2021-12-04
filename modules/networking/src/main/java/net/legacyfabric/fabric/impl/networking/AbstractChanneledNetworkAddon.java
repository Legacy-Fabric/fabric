/*
 * Copyright (c) 2021 Legacy Rewoven
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.networking;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;
import net.legacyfabric.fabric.api.networking.v1.PacketSender;

/**
 * A network addon which is aware of the channels the other side may receive.
 *
 * @param <H> the channel handler type
 */
public abstract class AbstractChanneledNetworkAddon<H> extends AbstractNetworkAddon<H> implements PacketSender {
	protected final ClientConnection connection;
	protected final GlobalReceiverRegistry<H> receiver;
	protected final Set<String> sendableChannels;
	protected final Set<String> sendableChannelsView;

	protected AbstractChanneledNetworkAddon(GlobalReceiverRegistry<H> receiver, ClientConnection connection, String description) {
		this(receiver, connection, new HashSet<>(), description);
	}

	protected AbstractChanneledNetworkAddon(GlobalReceiverRegistry<H> receiver, ClientConnection connection, Set<String> sendableChannels, String description) {
		super(receiver, description);
		this.connection = connection;
		this.receiver = receiver;
		this.sendableChannels = sendableChannels;
		this.sendableChannelsView = Collections.unmodifiableSet(sendableChannels);
	}

	public abstract void lateInit();

	protected void registerPendingChannels(ChannelInfoHolder holder) {
		final Collection<String> pending = holder.getPendingChannelsNames();

		if (!pending.isEmpty()) {
			register(new ArrayList<>(pending));
			pending.clear();
		}
	}

	// always supposed to handle async!
	protected boolean handle(String channelName, PacketByteBuf originalBuf) {
		this.logger.debug("Handling inbound packet from channel with name \"{}\"", channelName);

		// Handle reserved packets
		if (NetworkingImpl.REGISTER_CHANNEL.equals(channelName)) {
			this.receiveRegistration(true, PacketByteBufs.slice(originalBuf));
			return true;
		}

		if (NetworkingImpl.UNREGISTER_CHANNEL.equals(channelName)) {
			this.receiveRegistration(false, PacketByteBufs.slice(originalBuf));
			return true;
		}

		H handler = this.getHandler(channelName);

		if (handler == null) {
			return false;
		}

		PacketByteBuf buf = PacketByteBufs.slice(originalBuf);

		try {
			this.receive(handler, buf);
		} catch (Throwable ex) {
			this.logger.error("Encountered exception while handling in channel with name \"{}\"", channelName, ex);
			throw ex;
		}

		return true;
	}

	protected abstract void receive(H handler, PacketByteBuf buf);

	protected void sendInitialChannelRegistrationPacket() {
		final PacketByteBuf buf = this.createRegistrationPacket(this.getReceivableChannels());

		if (buf != null) {
			this.sendPacket(NetworkingImpl.REGISTER_CHANNEL, buf);
		}
	}

	protected PacketByteBuf createRegistrationPacket(Collection<String> channels) {
		if (channels.isEmpty()) {
			return null;
		}

		PacketByteBuf buf = PacketByteBufs.create();
		boolean first = true;

		for (String channel : channels) {
			if (first) {
				first = false;
			} else {
				buf.writeByte(0);
			}

			buf.writeBytes(channel.getBytes(StandardCharsets.US_ASCII));
		}

		return buf;
	}

	// wrap in try with res (buf)
	protected void receiveRegistration(boolean register, PacketByteBuf buf) {
		List<String> ids = new ArrayList<>();
		StringBuilder active = new StringBuilder();

		while (buf.isReadable()) {
			byte b = buf.readByte();

			if (b != 0) {
				active.append((char) b);
			} else {
				this.addId(ids, active);
				active = new StringBuilder();
			}
		}

		this.addId(ids, active);
		this.schedule(register ? () -> register(ids) : () -> unregister(ids));
	}

	void register(List<String> ids) {
		this.sendableChannels.addAll(ids);
		this.invokeRegisterEvent(ids);
	}

	void unregister(List<String> ids) {
		this.sendableChannels.removeAll(ids);
		this.invokeUnregisterEvent(ids);
	}

	@Override
	public void sendPacket(Packet<?> packet) {
		Objects.requireNonNull(packet, "Packet cannot be null");

		this.connection.send(packet);
	}

	@Override
	public void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback) {
		Objects.requireNonNull(packet, "Packet cannot be null");

		this.connection.send(packet, callback);
	}

	/**
	 * Schedules a task to run on the main thread.
	 */
	protected abstract void schedule(Runnable task);

	protected abstract void invokeRegisterEvent(List<String> ids);

	protected abstract void invokeUnregisterEvent(List<String> ids);

	private void addId(List<String> ids, StringBuilder sb) {
		String literal = sb.toString();
		ids.add(literal);
	}

	public Set<String> getSendableChannels() {
		return this.sendableChannelsView;
	}
}
