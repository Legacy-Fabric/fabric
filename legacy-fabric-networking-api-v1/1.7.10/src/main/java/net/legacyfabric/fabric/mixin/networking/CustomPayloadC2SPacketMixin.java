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

package net.legacyfabric.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;
import net.legacyfabric.fabric.impl.networking.server.CustomPayloadC2SPacketExtension;

@Mixin(CustomPayloadC2SPacket.class)
public abstract class CustomPayloadC2SPacketMixin implements CustomPayloadC2SPacketExtension {
	@Shadow
	public abstract byte[] method_7981();

	@Override
	public PacketByteBuf getData() {
		return new PacketByteBuf(PacketByteBufs.empty().writeBytes(this.method_7981()));
	}
}
