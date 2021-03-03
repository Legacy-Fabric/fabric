/*
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

package net.fabricmc.fabric.api.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.fabric.impl.network.ServerSidePacketRegistryImpl;

public interface ServerSidePacketRegistry extends PacketRegistry {
	ServerSidePacketRegistry INSTANCE = new ServerSidePacketRegistryImpl();

	/**
	 * @deprecated Use {@link #canPlayerReceive(PlayerEntity, PacketIdentifier)} (PacketIdentifier)} instead!
	 */
	@Deprecated
	boolean canPlayerReceive(PlayerEntity player, String id);

	boolean canPlayerReceive(PlayerEntity player, PacketIdentifier id);

	void sendToPlayer(PlayerEntity player, Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener);

	default void sendToPlayer(PlayerEntity player, PacketIdentifier id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		sendToPlayer(player, toPacket(id, buf), completionListener);
	}

	/**
	 * @deprecated Use {@link #sendToPlayer(PlayerEntity, Packet, GenericFutureListener)}  instead!
	 */
	@Deprecated
	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		LogManager.getLogger().warn("S2C Packet with id {} uses deprecated String ids! Support is not guaranteed", id);
		sendToPlayer(player, toPacket(id, buf), completionListener);
	}

	default void sendToPlayer(PlayerEntity player, Packet<?> packet) {
		sendToPlayer(player, packet, null);
	}

	default void sendToPlayer(PlayerEntity player, PacketIdentifier id, PacketByteBuf buf) {
		sendToPlayer(player, toPacket(id, buf), null);
	}

	/**
	 * @deprecated Use {@link #sendToPlayer(PlayerEntity, PacketIdentifier, PacketByteBuf)} instead!
	 */
	@Deprecated
	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf) {
		LogManager.getLogger().warn("S2C Packet with id {} uses deprecated String ids! Support is not guaranteed", id);
		sendToPlayer(player, id, buf, null);
	}
}
