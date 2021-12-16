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

package io.github.legacyrewoven.impl.networking.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.github.legacyrewoven.api.networking.v1.PacketByteBufs;
import io.github.legacyrewoven.api.networking.v1.S2CPlayChannelEvents;
import io.github.legacyrewoven.api.networking.v1.ServerPlayConnectionEvents;
import io.github.legacyrewoven.api.networking.v1.ServerPlayNetworking;
import io.github.legacyrewoven.impl.networking.AbstractChanneledNetworkAddon;
import io.github.legacyrewoven.impl.networking.ChannelInfoHolder;
import io.github.legacyrewoven.impl.networking.NetworkingImpl;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.PacketByteBuf;

public final class ServerPlayNetworkAddon extends AbstractChanneledNetworkAddon<ServerPlayNetworking.PlayChannelHandler> {
	private final ServerPlayNetworkHandler handler;
	private final MinecraftServer server;
	private boolean sentInitialRegisterPacket;

	public ServerPlayNetworkAddon(ServerPlayNetworkHandler handler, MinecraftServer server) {
		super(ServerNetworkingImpl.PLAY, handler.connection, "ServerPlayNetworkAddon for " + handler.player.getGameProfile().getName());
		this.handler = handler;
		this.server = server;

		// Must register pending channels via lateinit
		this.registerPendingChannels((ChannelInfoHolder) this.connection);

		// Register global receivers and attach to session
		this.receiver.startSession(this);
	}

	@Override
	public void lateInit() {
		for (Map.Entry<String, ServerPlayNetworking.PlayChannelHandler> entry : this.receiver.getHandlers().entrySet()) {
			this.registerChannel(entry.getKey(), entry.getValue());
		}

		ServerPlayConnectionEvents.INIT.invoker().onPlayInit(this.handler, this.server);
	}

	public void onClientReady() {
		ServerPlayConnectionEvents.JOIN.invoker().onPlayReady(this.handler, this, this.server);

		this.sendInitialChannelRegistrationPacket();
		this.sentInitialRegisterPacket = true;
	}

	/**
	 * Handles an incoming packet.
	 *
	 * @param packet the packet to handle
	 * @return true if the packet has been handled
	 */
	public boolean handle(CustomPayloadC2SPacket packet) {
		//TODO: Do not handle the packet on game thread
		if (((MinecraftServerExtensions) this.server).isOnThread()) {
			return false;
		}

		//hack to work around the fact that for some reason le PacketByteBufs don't work the same
		byte[] data = packet.getData();
		PacketByteBuf payload = new PacketByteBuf(PacketByteBufs.create().writeBytes(data));
		return this.handle(packet.getChannel(), payload);
	}

	@Override
	protected void receive(ServerPlayNetworking.PlayChannelHandler handler, PacketByteBuf buf) {
		handler.receive(this.server, this.handler.player, this.handler, buf, this);
	}

	// impl details
	//TODO: WTF
	@Override
	protected void schedule(Runnable task) {
		System.out.println("Sheduel");
		((MinecraftServerExtensions)this.handler.player.server).execute(task);
	}

	@Override
	public Packet createPacket(String channelName, PacketByteBuf buf) {
		return ServerPlayNetworking.createS2CPacket(channelName, buf);
	}

	@Override
	protected void invokeRegisterEvent(List<String> ids) {
		S2CPlayChannelEvents.REGISTER.invoker().onChannelRegister(this.handler, this, this.server, ids);
	}

	@Override
	protected void invokeUnregisterEvent(List<String> ids) {
		S2CPlayChannelEvents.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.server, ids);
	}

	@Override
	protected void handleRegistration(String channelName) {
		// If we can already send packets, immediately send the register packet for this channel
		if (this.sentInitialRegisterPacket) {
			final PacketByteBuf buf = this.createRegistrationPacket(Collections.singleton(channelName));

			if (buf != null) {
				this.sendPacket(NetworkingImpl.REGISTER_CHANNEL, buf);
			}
		}
	}

	@Override
	protected void handleUnregistration(String channelName) {
		// If we can already send packets, immediately send the unregister packet for this channel
		if (this.sentInitialRegisterPacket) {
			final PacketByteBuf buf = this.createRegistrationPacket(Collections.singleton(channelName));

			if (buf != null) {
				this.sendPacket(NetworkingImpl.UNREGISTER_CHANNEL, buf);
			}
		}
	}

	@Override
	public void invokeDisconnectEvent() {
		ServerPlayConnectionEvents.DISCONNECT.invoker().onPlayDisconnect(this.handler, this.server);
		this.receiver.endSession(this);
	}

	@Override
	protected boolean isReservedChannel(String channelName) {
		return NetworkingImpl.isReservedPlayChannel(channelName);
	}
}
