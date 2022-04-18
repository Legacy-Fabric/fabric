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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityCombatEvents;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends EntityMixin {
	@Inject(method = "onKilled", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;onKilledOther(Lnet/minecraft/entity/LivingEntity;)V"))
	private void onEntityKilledOther(DamageSource source, CallbackInfo ci) {
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.invoker().afterKilledOtherEntity(source.getAttacker(), (LivingEntity) (Object) this);
	}
}
