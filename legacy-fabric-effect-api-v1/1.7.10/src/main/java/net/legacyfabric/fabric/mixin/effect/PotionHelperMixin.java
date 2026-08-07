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

import java.util.HashMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.IntegerMapMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.living.effect.PotionHelper;

import net.legacyfabric.fabric.impl.effect.versioned.StatusEffectRegistryImpl;

@Mixin(PotionHelper.class)
public class PotionHelperMixin {
	@Shadow
	@Final
	private static HashMap<Integer, String> DURATION_RECIPES;

	@Shadow
	@Final
	private static HashMap<Integer, String> AMPLIFIER_RECIPES;

	@Shadow
	@Final
	private static HashMap<Integer, Integer> COLOR_CACHE;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void lf$registerRemappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(StatusEffectRegistryImpl.KEY, NamespacedIdentifiers.from("potion/duration_recipes"), IntegerMapMapper.of(DURATION_RECIPES));
		SyncedRegistries.registerMapper(StatusEffectRegistryImpl.KEY, NamespacedIdentifiers.from("potion/amplifier_recipes"), IntegerMapMapper.of(AMPLIFIER_RECIPES));
		SyncedRegistries.registerMapper(StatusEffectRegistryImpl.KEY, NamespacedIdentifiers.from("potion/color_cache"), IntegerMapMapper.of(COLOR_CACHE));
	}
}
