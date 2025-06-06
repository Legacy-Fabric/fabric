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

package net.legacyfabric.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TrackedEntityInstance;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.legacyfabric.fabric.api.networking.v1.EntityTrackingEvents;

@Mixin(TrackedEntityInstance.class)
abstract class EntityTrackerEntryMixin {
	@Shadow
	public Entity trackedEntity;

	@Inject(method = "removeTrackingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;stopTracking(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER))
	private void onStopTracking1(ServerPlayerEntity player, CallbackInfo ci) {
		EntityTrackingEvents.STOP_TRACKING.invoker().onStopTracking(this.trackedEntity, player);
	}

	@Inject(method = "method_2180", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false))
	private void onStopTracking2(ServerPlayerEntity player, CallbackInfo ci) {
		EntityTrackingEvents.STOP_TRACKING.invoker().onStopTracking(this.trackedEntity, player);
	}

	@Inject(method = "method_2184", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false))
	private void onStopTracking3(ServerPlayerEntity player, CallbackInfo ci) {
		EntityTrackingEvents.STOP_TRACKING.invoker().onStopTracking(this.trackedEntity, player);
	}

	@Inject(method = "method_2184", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Ljava/util/Set;add(Ljava/lang/Object;)Z", remap = false))
	private void onStartTracking(ServerPlayerEntity player, CallbackInfo ci) {
		EntityTrackingEvents.START_TRACKING.invoker().onStartTracking(this.trackedEntity, player);
	}
}
