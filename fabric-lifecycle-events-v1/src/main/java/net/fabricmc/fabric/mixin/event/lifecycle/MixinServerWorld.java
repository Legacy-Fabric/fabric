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

package net.fabricmc.fabric.mixin.event.lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;tick()V", shift = At.Shift.AFTER), method = "tick")
	public void startWorldTick(CallbackInfo ci) {
		ServerTickEvents.START_WORLD_TICK.invoker().onStartTick((ServerWorld) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void endWorldTick(CallbackInfo ci) {
		ServerTickEvents.END_WORLD_TICK.invoker().onEndTick((ServerWorld) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "onEntitySpawned")
	public void loadEntity(Entity entity, CallbackInfo ci) {
		ServerEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, (ServerWorld) (Object) this);
	}
}
