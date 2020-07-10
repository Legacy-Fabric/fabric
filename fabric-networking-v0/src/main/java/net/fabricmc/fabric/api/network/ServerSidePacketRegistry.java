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

package net.fabricmc.fabric.api.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.impl.network.ServerSidePacketRegistryImpl;

public interface ServerSidePacketRegistry extends PacketRegistry {
	ServerSidePacketRegistry INSTANCE = new ServerSidePacketRegistryImpl();

	boolean canPlayerReceive(PlayerEntity player, String id);

	void sendToPlayer(PlayerEntity player, Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener);

	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		sendToPlayer(player, toPacket(id, buf), completionListener);
	}

	default void sendToPlayer(PlayerEntity player, Packet<?> packet) {
		sendToPlayer(player, packet, null);
	}

	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf) {
		sendToPlayer(player, id, buf, null);
	}
}
