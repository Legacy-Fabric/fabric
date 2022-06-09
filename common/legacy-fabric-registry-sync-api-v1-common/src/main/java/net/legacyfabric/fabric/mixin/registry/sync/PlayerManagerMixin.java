/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.integrated.IntegratedServer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import net.legacyfabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 2), method = "onPlayerConnect")
	public void playerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		if (fabric_shouldSend()) {
			player.networkHandler.sendPacket(ServerPlayNetworking.createS2CPacket(RegistryRemapperAccess.PACKET_ID, ((RegistryRemapperAccess) this.server).createBuf()));
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
