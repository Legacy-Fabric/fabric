/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.networking.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.networking.client.ClientNetworkingImpl;
import net.legacyfabric.fabric.impl.networking.client.ClientPlayNetworkAddon;
import net.legacyfabric.fabric.impl.networking.client.ClientPlayNetworkHandlerExtensions;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayNetworkHandler.class, priority = 999)
abstract class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerExtensions {
	@Shadow
	private MinecraftClient client;

	@Unique
	private ClientPlayNetworkAddon addon;

	@Override
	public void lf$initAddon() {
		this.addon = new ClientPlayNetworkAddon((ClientPlayNetworkHandler) (Object) this, this.client);
		// A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
		ClientNetworkingImpl.setClientPlayAddon(this.addon);
		this.addon.lateInit();
	}

	@Inject(method = "onGameJoin", at = @At("RETURN"))
	private void handleServerPlayReady(GameJoinS2CPacket packet, CallbackInfo ci) {
		this.addon.onServerReady();
	}

	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void handleCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
		if (this.addon.handle(packet)) {
			// Do not cancel minecraft's packets
			if (!packet.getChannel().startsWith("MC")) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "onDisconnected", at = @At("HEAD"))
	private void handleDisconnection(Text reason, CallbackInfo ci) {
		this.addon.invokeDisconnectEvent();
	}

	@Override
	public ClientPlayNetworkAddon getAddon() {
		return this.addon;
	}
}
