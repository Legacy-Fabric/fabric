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

package net.legacyfabric.fabric.mixin.registry.sync.versioned;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import net.legacyfabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/handler/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 2), method = "onLogin")
	public void playerConnect(Connection connection, ServerPlayerEntity player, CallbackInfo ci) {
		if (fabric_shouldSend()) {
			ServerPlayNetworking.send(player, RegistryHelperImplementation.PACKET_ID, RegistryHelperImplementation.createBuf());
		}
	}

	@Unique
	private boolean fabric_shouldSend() {
		boolean published = false;

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			published = fabric_isPublished();
		}

		return this.server.isDedicated() || published;
	}

	@Environment(EnvType.CLIENT)
	@Unique
	private boolean fabric_isPublished() {
		return ((IntegratedServer) this.server).isPublished();
	}
}
