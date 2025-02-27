/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import net.minecraft.entity.effect.StatusEffectStrings;

@Mixin(StatusEffectStrings.class)
public interface StatusEffectStringsAccessor {
	@Accessor
	static HashMap<Integer, String> getField_4424() {
		return null;
	}

	@Accessor
	static void setField_4424(HashMap<Integer, String> value) {
	}

	@Accessor
	static HashMap<Integer, String> getField_4425() {
		return null;
	}

	@Accessor
	static void setField_4425(HashMap<Integer, String> value) {
	}

	@Accessor
	static HashMap<Integer, Integer> getField_4426() {
		return null;
	}

	@Accessor
	static void setField_4426(HashMap<Integer, Integer> value) {
	}
}
