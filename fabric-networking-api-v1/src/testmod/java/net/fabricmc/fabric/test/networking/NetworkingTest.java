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

package net.fabricmc.fabric.test.networking;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkingTest implements ClientModInitializer, ModInitializer {
	private static final String PARTICLE_PACKET_ID = "FABRIC|ParticlePacket";

	@Override
	public void onInitializeClient() {
		KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("fabric:test_keybinding", Keyboard.KEY_R, "fabric.key.categories.test"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (keyBinding.wasPressed()) {
				client.player.sendChatMessage("Key was pressed!");
				ClientPlayNetworking.send(PARTICLE_PACKET_ID, PacketByteBufs.empty());
			}
		});
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(PARTICLE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			((ServerWorld) player.world).addParticle(ParticleType.ANGRY_VILLAGER, player.x, player.y + 1, player.z, 1, 0, 0.5, 0, 0.1);
		});
	}
}
