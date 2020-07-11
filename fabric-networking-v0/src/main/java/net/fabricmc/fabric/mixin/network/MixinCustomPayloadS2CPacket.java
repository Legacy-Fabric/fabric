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

package net.fabricmc.fabric.mixin.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.fabric.impl.network.CustomPayloadPacketAccessor;

@Mixin(value = CustomPayloadS2CPacket.class, priority = 500)
public class MixinCustomPayloadS2CPacket implements CustomPayloadPacketAccessor {
	@Shadow
	private String field_6157;

	@Shadow
	private PacketByteBuf field_6158;

	@Override
	public String getChannel() {
		return this.field_6157;
	}

	@Override
	public PacketByteBuf getData() {
		return this.field_6158;
	}
}
