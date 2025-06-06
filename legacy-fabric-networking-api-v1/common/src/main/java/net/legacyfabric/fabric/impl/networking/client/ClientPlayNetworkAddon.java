/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.impl.networking.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacyfabric.fabric.impl.networking.AbstractChanneledNetworkAddon;
import net.legacyfabric.fabric.impl.networking.ChannelInfoHolder;
import net.legacyfabric.fabric.impl.networking.NetworkingImpl;

@Environment(EnvType.CLIENT)
public final class ClientPlayNetworkAddon extends AbstractChanneledNetworkAddon<ClientPlayNetworking.PlayChannelHandler> {
	private final ClientPlayNetworkHandler handler;
	private final MinecraftClient client;
	private boolean sentInitialRegisterPacket;

	public ClientPlayNetworkAddon(ClientPlayNetworkHandler handler, MinecraftClient client) {
		super(ClientNetworkingImpl.PLAY, handler.getClientConnection(), "ClientPlayNetworkAddon for " + getPlayerName(handler));
		this.handler = handler;
		this.client = client;

		// Must register pending channels via lateinit
		this.registerPendingChannels((ChannelInfoHolder) this.connection);

		// Register global receivers and attach to session
		this.receiver.startSession(this);
	}

	private static String getPlayerName(ClientPlayNetworkHandler handler) {
		try {
			return handler.getProfile().getName();
		} catch (NoSuchMethodError e) {
			return handler.toString();
		}
	}

	@Override
	public void lateInit() {
		for (Map.Entry<String, ClientPlayNetworking.PlayChannelHandler> entry : this.receiver.getHandlers().entrySet()) {
			this.registerChannel(entry.getKey(), entry.getValue());
		}

		ClientPlayConnectionEvents.INIT.invoker().onPlayInit(this.handler, this.client);
	}

	public void onServerReady() {
		ClientPlayConnectionEvents.JOIN.invoker().onPlayReady(this.handler, this, this.client);

		// The client cannot send any packets, including `minecraft:register` until after GameJoinS2CPacket is received.
		this.sendInitialChannelRegistrationPacket();
		this.sentInitialRegisterPacket = true;
	}

	/**
	 * Handles an incoming packet.
	 *
	 * @param packet the packet to handle
	 * @return true if the packet has been handled
	 */
	public boolean handle(CustomPayloadS2CPacket packet) {
		// Do not handle the packet on game thread
		if (((MinecraftClientExtensions) this.client).isOnGameThread()) {
			return false;
		}

		PacketByteBuf buf = new PacketByteBuf(((CustomPayloadS2CPacketExtension) packet).getData().copy());

		try {
			return this.handle(packet.getChannel(), buf);
		} finally {
			buf.release();
		}
	}

	@Override
	protected void receive(ClientPlayNetworking.PlayChannelHandler handler, PacketByteBuf buf) {
		handler.receive(this.client, this.handler, buf, this);
	}

	// impl details

	@Override
	protected void schedule(Runnable task) {
		MinecraftClient.getInstance().submit(task);
	}

	@Override
	public Packet<?> createPacket(String channelName, PacketByteBuf buf) {
		return ClientPlayNetworking.createC2SPacket(channelName, buf);
	}

	@Override
	protected void invokeRegisterEvent(List<String> ids) {
		C2SPlayChannelEvents.REGISTER.invoker().onChannelRegister(this.handler, this, this.client, ids);
	}

	@Override
	protected void invokeUnregisterEvent(List<String> ids) {
		C2SPlayChannelEvents.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.client, ids);
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
		ClientPlayConnectionEvents.DISCONNECT.invoker().onPlayDisconnect(this.handler, this.client);
		this.receiver.endSession(this);
	}

	@Override
	protected boolean isReservedChannel(String channelName) {
		return NetworkingImpl.isReservedPlayChannel(channelName);
	}
}
