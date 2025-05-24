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

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.effect.StatusEffectStrings;

@Mixin(StatusEffectStrings.class)
public interface StatusEffectStringsAccessor {
	@Accessor("field_9162")
	static Map<Integer, String> getLevelsMap() {
		return null;
	}

	@Accessor("field_9162")
	static void setLevelsMap(Map<Integer, String> value) {
	}

	@Accessor("field_9163")
	static Map<Integer, String> getAmplifyingFactorsMap() {
		return null;
	}

	@Accessor("field_9163")
	static void setAmplifyingFactorsMap(Map<Integer, String> value) {
	}

	@Accessor("field_9164")
	static Map<Integer, Integer> getEffectColorsMap() {
		return null;
	}

	@Accessor("field_9164")
	static void setEffectColorsMap(Map<Integer, Integer> value) {
	}
}
