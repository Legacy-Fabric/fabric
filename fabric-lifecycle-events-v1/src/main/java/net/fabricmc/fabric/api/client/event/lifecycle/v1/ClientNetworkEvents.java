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

package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public class ClientNetworkEvents {
	/**
	 * Called when the client receives a {@link CustomPayloadS2CPacket}.
	 * Useful when making server plugin companion mods.
	 */
	public static final Event<CustomPayload> CUSTOM_PAYLOAD = EventFactory.createArrayBacked(CustomPayload.class, listeners -> (channel, data) -> {
		for (CustomPayload callback : listeners) {
			callback.onCustomPayload(channel, data);
		}
	});

	@FunctionalInterface
	public interface CustomPayload {
		void onCustomPayload(String channel, PacketByteBuf data);
	}
}
