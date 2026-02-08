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

package net.legacyfabric.fabric.mixin.effect;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.living.effect.PotionHelper;

@Mixin(PotionHelper.class)
public interface StatusEffectStringsAccessor {
	@Accessor("DURATION_RECIPES")
	static HashMap<Integer, String> getLevelsMap() {
		return null;
	}

	@Accessor("DURATION_RECIPES")
	static void setLevelsMap(HashMap<Integer, String> value) {
	}

	@Accessor("AMPLIFIER_RECIPES")
	static HashMap<Integer, String> getAmplifyingFactorsMap() {
		return null;
	}

	@Accessor("AMPLIFIER_RECIPES")
	static void setAmplifyingFactorsMap(HashMap<Integer, String> value) {
	}

	@Accessor("COLOR_CACHE")
	static HashMap<Integer, Integer> getEffectColorsMap() {
		return null;
	}

	@Accessor("COLOR_CACHE")
	static void setEffectColorsMap(HashMap<Integer, Integer> value) {
	}
}
