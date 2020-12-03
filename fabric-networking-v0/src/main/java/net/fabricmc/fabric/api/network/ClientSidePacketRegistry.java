/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.network.ClientSidePacketRegistryImpl;

@Environment(EnvType.CLIENT)
public interface ClientSidePacketRegistry extends PacketRegistry {
	ClientSidePacketRegistry INSTANCE = new ClientSidePacketRegistryImpl();

	/**
	 * @deprecated Use {@link #canServerReceive(PacketIdentifier)} instead!
	 */
	@Deprecated
	boolean canServerReceive(String id);

	boolean canServerReceive(PacketIdentifier id);

	void sendToServer(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener);

	default void sendToServer(PacketIdentifier id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		sendToServer(this.toPacket(id, buf), completionListener);
	}

	/**
	 * @deprecated Use {@link #sendToServer(PacketIdentifier, PacketByteBuf, GenericFutureListener)} instead!
	 */
	@Deprecated
	default void sendToServer(String id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		LogManager.getLogger().warn("C2S Packet with id {} uses deprecated String ids! Support is not guaranteed", id);
		sendToServer(this.toPacket(id, buf), completionListener);
	}

	default void sendToServer(Packet<?> packet) {
		sendToServer(packet, null);
	}

	default void sendToServer(PacketIdentifier id, PacketByteBuf buf) {
		sendToServer(id, buf, null);
	}

	/**
	 * @deprecated Use {@link #sendToServer(PacketIdentifier, PacketByteBuf)} )} instead!
	 */
	@Deprecated
	default void sendToServer(String id, PacketByteBuf buf) {
		LogManager.getLogger().warn("C2S Packet with id {} uses deprecated String ids! Support is not guaranteed", id);
		sendToServer(id, buf, null);
	}
}
