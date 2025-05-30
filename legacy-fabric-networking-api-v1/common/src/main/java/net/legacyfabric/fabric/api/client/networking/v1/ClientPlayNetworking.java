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

package net.legacyfabric.fabric.api.client.networking.v1;

import java.util.Objects;
import java.util.Set;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.ThreadExecutor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.networking.v1.PacketSender;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.networking.client.ClientNetworkingImpl;
import net.legacyfabric.fabric.impl.networking.client.ClientPlayNetworkAddon;

/**
 * Offers access to play stage client-side networking functionalities.
 *
 * <p>Client-side networking functionalities include receiving clientbound packets,
 * sending serverbound packets, and events related to client-side network handlers.
 *
 * <p>This class should be only used on the physical client and for the logical client.
 */
@Environment(EnvType.CLIENT)
public final class ClientPlayNetworking {
	/**
	 * Registers a handler to a channel.
	 * A global receiver is registered to all connections, in the present and future.
	 *
	 * <p>If a handler is already registered to the {@code channel}, this method will return {@code false}, and no change will be made.
	 * Use {@link #unregisterGlobalReceiver(String)} to unregister the existing handler.
	 *
	 * @param channelName    the id of the channel
	 * @param channelHandler the handler
	 * @return false if a handler is already registered to the channel
	 * @see ClientPlayNetworking#unregisterGlobalReceiver(String)
	 * @see ClientPlayNetworking#registerReceiver(String, PlayChannelHandler)
	 */
	public static boolean registerGlobalReceiver(String channelName, PlayChannelHandler channelHandler) {
		return ClientNetworkingImpl.PLAY.registerGlobalReceiver(channelName, channelHandler);
	}

	/**
	 * Registers a handler to a channel.
	 * A global receiver is registered to all connections, in the present and future.
	 *
	 * <p>If a handler is already registered to the {@code channel}, this method will return {@code false}, and no change will be made.
	 * Use {@link #unregisterGlobalReceiver(Identifier)} to unregister the existing handler.
	 *
	 * @param channelId      the id of the channel
	 * @param channelHandler the handler
	 * @return false if a handler is already registered to the channel
	 * @see ClientPlayNetworking#unregisterGlobalReceiver(Identifier)
	 * @see ClientPlayNetworking#registerReceiver(Identifier, PlayChannelHandler)
	 */
	public static boolean registerGlobalReceiver(Identifier channelId, PlayChannelHandler channelHandler) {
		return registerGlobalReceiver(channelId.toString(), channelHandler);
	}

	/**
	 * Removes the handler of a channel.
	 * A global receiver is registered to all connections, in the present and future.
	 *
	 * <p>The {@code channel} is guaranteed not to have a handler after this call.
	 *
	 * @param channelName the id of the channel
	 * @return the previous handler, or {@code null} if no handler was bound to the channel
	 * @see ClientPlayNetworking#registerGlobalReceiver(String, PlayChannelHandler)
	 * @see ClientPlayNetworking#unregisterReceiver(String)
	 */
	public static PlayChannelHandler unregisterGlobalReceiver(String channelName) {
		return ClientNetworkingImpl.PLAY.unregisterGlobalReceiver(channelName);
	}

	/**
	 * Removes the handler of a channel.
	 * A global receiver is registered to all connections, in the present and future.
	 *
	 * <p>The {@code channel} is guaranteed not to have a handler after this call.
	 *
	 * @param channelId the id of the channel
	 * @return the previous handler, or {@code null} if no handler was bound to the channel
	 * @see ClientPlayNetworking#registerGlobalReceiver(Identifier, PlayChannelHandler)
	 * @see ClientPlayNetworking#unregisterReceiver(Identifier)
	 */
	public static PlayChannelHandler unregisterGlobalReceiver(Identifier channelId) {
		return unregisterGlobalReceiver(channelId.toString());
	}

	/**
	 * Gets all channel names which global receivers are registered for.
	 * A global receiver is registered to all connections, in the present and future.
	 *
	 * @return all channel names which global receivers are registered for.
	 */
	public static Set<String> getGlobalReceivers() {
		return ClientNetworkingImpl.PLAY.getChannels();
	}

	/**
	 * Registers a handler to a channel.
	 *
	 * <p>If a handler is already registered to the {@code channel}, this method will return {@code false}, and no change will be made.
	 * Use {@link #unregisterReceiver(String)} to unregister the existing handler.
	 *
	 * @param channelName the id of the channel
	 * @return false if a handler is already registered to the channel
	 * @throws IllegalStateException if the client is not connected to a server
	 * @see ClientPlayConnectionEvents#INIT
	 */
	public static boolean registerReceiver(String channelName, PlayChannelHandler channelHandler) {
		final ClientPlayNetworkAddon addon = ClientNetworkingImpl.getClientPlayAddon();

		if (addon != null) {
			return addon.registerChannel(channelName, channelHandler);
		}

		throw new IllegalStateException("Cannot register receiver while not in game!");
	}

	/**
	 * Registers a handler to a channel.
	 *
	 * <p>If a handler is already registered to the {@code channel}, this method will return {@code false}, and no change will be made.
	 * Use {@link #unregisterReceiver(String)} to unregister the existing handler.
	 *
	 * @param channelId the id of the channel
	 * @return false if a handler is already registered to the channel
	 * @throws IllegalStateException if the client is not connected to a server
	 * @see ClientPlayConnectionEvents#INIT
	 */
	public static boolean registerReceiver(Identifier channelId, PlayChannelHandler channelHandler) {
		return registerReceiver(channelId.toString(), channelHandler);
	}

	/**
	 * Removes the handler of a channel.
	 *
	 * <p>The {@code channelName} is guaranteed not to have a handler after this call.
	 *
	 * @param channelName the id of the channel
	 * @return the previous handler, or {@code null} if no handler was bound to the channel
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static PlayChannelHandler unregisterReceiver(String channelName) throws IllegalStateException {
		final ClientPlayNetworkAddon addon = ClientNetworkingImpl.getClientPlayAddon();

		if (addon != null) {
			return addon.unregisterChannel(channelName);
		}

		throw new IllegalStateException("Cannot unregister receiver while not in game!");
	}

	/**
	 * Removes the handler of a channel.
	 *
	 * <p>The {@code channelId} is guaranteed not to have a handler after this call.
	 *
	 * @param channelId the id of the channel
	 * @return the previous handler, or {@code null} if no handler was bound to the channel
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static PlayChannelHandler unregisterReceiver(Identifier channelId) throws IllegalStateException {
		return unregisterReceiver(channelId.toString());
	}

	/**
	 * Gets all the channel names that the client can receive packets on.
	 *
	 * @return All the channel names that the client can receive packets on
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static Set<String> getReceived() throws IllegalStateException {
		final ClientPlayNetworkAddon addon = ClientNetworkingImpl.getClientPlayAddon();

		if (addon != null) {
			return addon.getReceivableChannels();
		}

		throw new IllegalStateException("Cannot get a list of channels the client can receive packets on while not in game!");
	}

	/**
	 * Gets all channel names that the connected server declared the ability to receive a packets on.
	 *
	 * @return All the channel names the connected server declared the ability to receive a packets on
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static Set<String> getSendable() throws IllegalStateException {
		final ClientPlayNetworkAddon addon = ClientNetworkingImpl.getClientPlayAddon();

		if (addon != null) {
			return addon.getSendableChannels();
		}

		throw new IllegalStateException("Cannot get a list of channels the server can receive packets on while not in game!");
	}

	/**
	 * Checks if the connected server declared the ability to receive a packet on a specified channel name.
	 *
	 * @param channelName the channel name
	 * @return True if the connected server has declared the ability to receive a packet on the specified channel.
	 * False if the client is not in game.
	 */
	public static boolean canSend(String channelName) throws IllegalArgumentException {
		// You cant send without a client player, so this is fine
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			return ClientNetworkingImpl.getAddon(MinecraftClient.getInstance().getNetworkHandler()).getSendableChannels().contains(channelName);
		}

		return false;
	}

	/**
	 * Checks if the connected server declared the ability to receive a packet on a specified channel name.
	 *
	 * @param channelId the channel name
	 * @return True if the connected server has declared the ability to receive a packet on the specified channel.
	 * False if the client is not in game.
	 */
	public static boolean canSend(Identifier channelId) throws IllegalArgumentException {
		return canSend(channelId.toString());
	}

	/**
	 * Creates a packet which may be sent to a the connected server.
	 *
	 * @param channelName the channel name
	 * @param buf         the packet byte buf which represents the payload of the packet
	 * @return a new packet
	 */
	public static Packet<?> createC2SPacket(String channelName, PacketByteBuf buf) {
		Objects.requireNonNull(channelName, "Channel name cannot be null");
		Objects.requireNonNull(buf, "Buf cannot be null");

		return ClientNetworkingImpl.createPlayC2SPacket(channelName, buf);
	}

	/**
	 * Creates a packet which may be sent to a the connected server.
	 *
	 * @param channelId the channel name
	 * @param buf       the packet byte buf which represents the payload of the packet
	 * @return a new packet
	 */
	public static Packet<?> createC2SPacket(Identifier channelId, PacketByteBuf buf) {
		return createC2SPacket(channelId.toString(), buf);
	}

	/**
	 * Gets the packet sender which sends packets to the connected server.
	 *
	 * @return the client's packet sender
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static PacketSender getSender() throws IllegalStateException {
		// You cant send without a client player, so this is fine
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			return ClientNetworkingImpl.getAddon(MinecraftClient.getInstance().getNetworkHandler());
		}

		throw new IllegalStateException("Cannot get packet sender when not in game!");
	}

	/**
	 * Sends a packet to the connected server.
	 *
	 * @param channelName the channel of the packet
	 * @param buf         the payload of the packet
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static void send(String channelName, PacketByteBuf buf) throws IllegalStateException {
		// You cant send without a client player, so this is fine
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			MinecraftClient.getInstance().getNetworkHandler().sendPacket(createC2SPacket(channelName, buf));
			return;
		}

		throw new IllegalStateException("Cannot send packets when not in game!");
	}

	/**
	 * Sends a packet to the connected server.
	 *
	 * @param channelId the channel of the packet
	 * @param buf       the payload of the packet
	 * @throws IllegalStateException if the client is not connected to a server
	 */
	public static void send(Identifier channelId, PacketByteBuf buf) throws IllegalStateException {
		// You cant send without a client player, so this is fine
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			MinecraftClient.getInstance().getNetworkHandler().sendPacket(createC2SPacket(channelId, buf));
			return;
		}

		throw new IllegalStateException("Cannot send packets when not in game!");
	}

	private ClientPlayNetworking() {
	}

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface PlayChannelHandler {
		/**
		 * Handles an incoming packet.
		 *
		 * <p>This method is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
		 * Modification to the game should be {@linkplain ThreadExecutor#execute(Runnable) scheduled} using the provided Minecraft client instance.
		 *
		 * <p>An example usage of this is to display an overlay message:
		 * <pre>{@code
		 * ClientPlayNetworking.registerReceiver(new String("mymod", "overlay"), (client, handler, buf, responseSender) -&rt; {
		 * 	String message = buf.readString(32767);
		 *
		 * 	// All operations on the server or world must be executed on the server thread
		 * 	client.execute(() -&rt; {
		 * 		client.inGameHud.setOverlayMessage(message, true);
		 *    });
		 * });
		 * }</pre>
		 *
		 * @param client         the client
		 * @param handler        the network handler that received this packet
		 * @param buf            the payload of the packet
		 * @param responseSender the packet sender
		 */
		void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender);
	}
}
