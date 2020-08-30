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

import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

public interface PacketRegistry {
	Packet<?> toPacket(PacketIdentifier id, PacketByteBuf buf);

	void register(PacketIdentifier id, PacketConsumer consumer);

	void unregister(PacketIdentifier id);

	/**
	 * @deprecated Use {@link #toPacket(PacketIdentifier, PacketByteBuf)} instead!
	 */
	@Deprecated
	Packet<?> toPacket(String id, PacketByteBuf buf);

	/**
	 * @deprecated Use {@link #register(PacketIdentifier, PacketConsumer)} instead!
	 */
	@Deprecated
	void register(String id, PacketConsumer consumer);

	/**
	 * @deprecated Use {@link #unregister(PacketIdentifier)} instead!
	 */
	@Deprecated
	void unregister(String id);
}
