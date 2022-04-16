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

package net.legacyfabric.fabric.mixin.entity.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.legacyfabric.fabric.api.entity.event.v1.ServerPlayerEvents;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends LivingEntityMixin {
	/**
	 * Minecraft by default does not call Entity#onKilledOther for a ServerPlayerEntity being killed.
	 * This is a Mojang bug.
	 * This is implements the method call on the server player entity and then calls the corresponding event.
	 */
	@Inject(method = "onKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;getOpponent()Lnet/minecraft/entity/LivingEntity;"))
	private void callOnKillForPlayer(DamageSource source, CallbackInfo ci) {
		final Entity attacker = source.getAttacker();

		// If the damage source that killed the player was an entity, then fire the event.
		if (attacker != null) {
			attacker.onKilledOther((ServerPlayerEntity) (Object) this);
			ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.invoker().afterKilledOtherEntity(attacker, (ServerPlayerEntity) (Object) this);
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void onCopyFrom(PlayerEntity player, boolean alive, CallbackInfo ci) {
		ServerPlayerEvents.COPY_FROM.invoker().copyFromPlayer((ServerPlayerEntity) player, (ServerPlayerEntity) (Object) this, alive);
	}
}
