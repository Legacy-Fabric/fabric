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

package net.legacyfabric.fabric.mixin.networking.versioned.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.networking.client.ClientPlayNetworkHandlerExtensions;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayNetworkHandler.class, priority = 999)
abstract class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerExtensions {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void initAddon(CallbackInfo ci) {
		this.lf$initAddon();
	}
}
