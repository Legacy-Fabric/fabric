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

import net.minecraft.util.PacketByteBuf;
import net.minecraft.network.Packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.network.ClientSidePacketRegistryImpl;

@Environment(EnvType.CLIENT)
public interface ClientSidePacketRegistry extends PacketRegistry {
	ClientSidePacketRegistry INSTANCE = new ClientSidePacketRegistryImpl();

	boolean canServerReceive(String id);

	void sendToServer(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener);

	default void sendToServer(String id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		sendToServer(this.toPacket(id, buf), completionListener);
	}

	default void sendToServer(Packet<?> packet) {
		sendToServer(packet, null);
	}

	default void sendToServer(String id, PacketByteBuf buf) {
		sendToServer(id, buf, null);
	}
}
