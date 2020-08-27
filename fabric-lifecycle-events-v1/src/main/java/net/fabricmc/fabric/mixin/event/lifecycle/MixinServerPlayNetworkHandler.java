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

package net.fabricmc.fabric.mixin.event.lifecycle;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerNetworkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
	@Shadow
	@Final
	public ClientConnection connection;

	@Shadow
	public ServerPlayerEntity player;

	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At("HEAD"), method = "disconnect")
	public void onPlayerDisconnect(String reason, CallbackInfo info) {
		ServerPlayerEvents.DISCONNECT.invoker().playerDisconnect(this.connection, this.player, this.server);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/ThreadExecutor;)V", shift = At.Shift.AFTER), method = "onCustomPayload")
	public void onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
		ServerNetworkEvents.CUSTOM_PAYLOAD.invoker().onCustomPayload(packet.getChannel(), packet.getPayload());
	}
}
