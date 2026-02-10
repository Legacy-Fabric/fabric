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

package net.legacyfabric.fabric.test.mixin.effect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import net.legacyfabric.fabric.test.registry.RegistryTest;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract EntityGroup getGroup();

	@Redirect(method = {"method_2490", "jump"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEffectInstance(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
	private StatusEffectInstance ourEffectJumpsHighAsWell(LivingEntity instance, StatusEffect effect) {
		StatusEffectInstance instance1 = instance.getEffectInstance(effect);

		if (instance1 == null) {
			instance1 = instance.getEffectInstance(RegistryTest.EFFECT);
		}

		return instance1;
	}

	@Redirect(method = {"jump"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
	private boolean hasOurEffectJumpsHighAsWell(LivingEntity instance, StatusEffect effect) {
		boolean instance1 = instance.hasStatusEffect(effect);

		if (!instance1) {
			instance1 = instance.hasStatusEffect(RegistryTest.EFFECT);
		}

		return instance1;
	}

	@Inject(method = "method_2658", at = @At("RETURN"), cancellable = true)
	private void efffffffect(StatusEffectInstance instance, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			if (this.getGroup() == EntityGroup.UNDEAD && instance.getEffectId() == RegistryTest.EFFECT.id) cir.setReturnValue(false);
		}
	}
}
