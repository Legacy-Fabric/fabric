/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.impl.network;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.PacketIdentifier;
import net.fabricmc.fabric.api.network.PacketRegistry;

public abstract class PacketRegistryImpl implements PacketRegistry {
	protected static final Logger LOGGER = LogManager.getLogger();
	protected final Map<String, PacketConsumer> consumerMap;

	PacketRegistryImpl() {
		consumerMap = Maps.newLinkedHashMap();
	}

	public static Optional<Packet<?>> createInitialRegisterPacket(PacketRegistry registry) {
		PacketRegistryImpl impl = (PacketRegistryImpl) registry;
		return impl.createRegisterTypePacket(PacketTypes.REGISTER, impl.consumerMap.keySet());
	}

	protected Optional<Packet<?>> createRegisterTypePacket(String id, Collection<String> ids) {
		if (ids.isEmpty()) {
			return Optional.empty();
		}

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		boolean first = true;

		for (String a : ids) {
			if (!first) {
				buf.writeByte(0);
			} else {
				first = false;
			}

			buf.writeBytes(a.getBytes(StandardCharsets.US_ASCII));
		}

		return Optional.of(toPacket(id, buf));
	}

	private boolean acceptRegisterType(String id, PacketContext context, Supplier<PacketByteBuf> bufSupplier) {
		Collection<String> ids = Sets.newHashSet();

		StringBuilder sb = new StringBuilder();
		char c;
		PacketByteBuf buf = bufSupplier.get();

		try {
			while (buf.readerIndex() < buf.writerIndex()) {
				c = (char) buf.readByte();

				if (c == 0) {
					this.addToCollection(id, ids, sb);

					sb = new StringBuilder();
				} else {
					sb.append(c);
				}
			}
		} finally {
			buf.release();
		}

		this.addToCollection(id, ids, sb);

		Collection<String> target = getIdCollectionFor(context);

		if (id.equals(PacketTypes.UNREGISTER)) {
			target.removeAll(ids);
			onReceivedUnregisterPacket(context, ids);
		} else {
			target.addAll(ids);
			onReceivedRegisterPacket(context, ids);
		}

		return false;
	}

	private void addToCollection(String id, Collection<String> ids, StringBuilder sb) {
		String s = sb.toString();

		if (!s.isEmpty()) {
			try {
				ids.add(s);
			} catch (Exception e) {
				LOGGER.warn("Received invalid identifier in " + id + ": " + s + " (" + e.getLocalizedMessage() + ")");
				LOGGER.trace(e);
			}
		}
	}

	protected abstract void onRegister(String id);

	protected abstract void onUnregister(String id);

	protected abstract Collection<String> getIdCollectionFor(PacketContext context);

	protected abstract void onReceivedRegisterPacket(PacketContext context, Collection<String> ids);

	protected abstract void onReceivedUnregisterPacket(PacketContext context, Collection<String> ids);

	@Override
	public void register(PacketIdentifier id, PacketConsumer consumer) {
		this.register(id.toString(), consumer);
	}

	@Override
	public void unregister(PacketIdentifier id) {
		this.unregister(id.toString());
	}

	@Override
	public void unregister(String id) {
		if (consumerMap.remove(id) != null) {
			onUnregister(id);
		} else {
			LOGGER.warn("Tried to unregister non-registered packet " + id + "!");
			LOGGER.trace(new Throwable());
		}
	}

	@Override
	public void register(String id, PacketConsumer consumer) {
		boolean isNew = true;

		if (consumerMap.containsKey(id)) {
			LOGGER.warn("Registered duplicate packet " + id + "!");
			LOGGER.trace(new Throwable());
			isNew = false;
		}

		consumerMap.put(id, consumer);

		if (isNew) {
			onRegister(id);
		}
	}

	public boolean accept(String id, PacketContext context, Supplier<PacketByteBuf> bufSupplier) {
		if (id.equals(PacketTypes.REGISTER) || id.equals(PacketTypes.UNREGISTER)) {
			return acceptRegisterType(id, context, bufSupplier);
		}

		PacketConsumer consumer = consumerMap.get(id);

		if (consumer != null) {
			PacketByteBuf buf = bufSupplier.get();

			try {
				consumer.accept(context, buf);
			} catch (Throwable t) {
				LOGGER.warn("Failed to handle packet " + id + "!", t);
			} finally {
				if (buf.refCnt() > 0 && !PacketDebugOptions.DISABLE_BUFFER_RELEASES) {
					buf.release();
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
