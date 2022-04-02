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

package net.legacyfabric.fabric.mixin.event.lifecycle.client;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;tick()V", shift = At.Shift.AFTER), method = "tick")
	public void startWorldTick(CallbackInfo ci) {
		ClientTickEvents.START_WORLD_TICK.invoker().onStartTick((ClientWorld) (Object) this);
	}

	@Inject(at = @At("RETURN"), method = "tick")
	public void endWorldTick(CallbackInfo ci) {
		ClientTickEvents.END_WORLD_TICK.invoker().onEndTick((ClientWorld) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "onEntitySpawned")
	public void unloadEntity(Entity entity, CallbackInfo ci) {
		ClientEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, (ClientWorld) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "onEntityRemoved")
	public void loadEntity(Entity entity, CallbackInfo ci) {
		ClientEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, (ClientWorld) (Object) this);
	}
}
