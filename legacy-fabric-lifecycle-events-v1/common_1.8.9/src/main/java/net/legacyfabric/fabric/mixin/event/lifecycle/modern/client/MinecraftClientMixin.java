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

package net.legacyfabric.fabric.mixin.event.lifecycle.modern.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	private void api$onStartTick(CallbackInfo info) {
		ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick((Minecraft) (Object) this);
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void api$onEndTick(CallbackInfo info) {
		ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick((Minecraft) (Object) this);
	}

	@Inject(at = @At(value = "RETURN"), method = "init")
	private void api$onStart(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STARTED.invoker().onClientStarted((Minecraft) (Object) this);
	}

	@Inject(at = @At(value = "HEAD"), method = "shutdown")
	private void api$onStopping(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoker().onClientStopping((Minecraft) (Object) this);
	}
}
