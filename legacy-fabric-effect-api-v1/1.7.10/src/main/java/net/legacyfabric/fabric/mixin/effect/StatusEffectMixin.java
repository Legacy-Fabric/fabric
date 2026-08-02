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

package net.legacyfabric.fabric.mixin.effect;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;
import net.ornithemc.osl.registries.api.registry.sync.ObjectArrayMapper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.api.effect.StatusEffectExtension;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;
import net.legacyfabric.fabric.impl.effect.versioned.StatusEffectRegistryImpl;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements StatusEffectExtension {
	@Mutable
	@Shadow
	@Final
	public static StatusEffect[] BY_ID;

	@Shadow
	private String key;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void lf$unlockRegistry(CallbackInfo ci) {
		StatusEffectRegistryImpl.unlock();
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		StatusEffectRegistryImpl.registerEffects();
		SyncedRegistries.registerMapper(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("status_effect/by_id"), ObjectArrayMapper.of(BY_ID));
	}

	@ModifyVariable(method = "<init>", argsOnly = true, ordinal = 0, at = @At("HEAD"))
	private static int lf$autoIdAssignment(int id) {
		if (id == REGISTRY_AUTO_ASSIGN_ID) {
			id = DynamicArrays.length(BY_ID);
		}

		return id;
	}

	@Inject(method = "<init>", at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/living/effect/StatusEffect;BY_ID:[Lnet/minecraft/entity/living/effect/StatusEffect;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
	))
	private void lf$growArray(int id, boolean harmful, int color, CallbackInfo ci) {
		int capacity = id + 1;

		BY_ID = DynamicArrays.grow(BY_ID, capacity);
	}

	@Inject(
			method = "getTranslationKey",
			at = @At(
					value = "HEAD"
			)
	)
	private void osl$blocks$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = StatusEffectRegistryImpl.getIdentifier((StatusEffect) (Object) this);

			if (identifier == null) {
				this.key = "potion.unknown";
			} else {
				this.key = Util.makeTranslationKey("potion", identifier);
			}
		}
	}
}
