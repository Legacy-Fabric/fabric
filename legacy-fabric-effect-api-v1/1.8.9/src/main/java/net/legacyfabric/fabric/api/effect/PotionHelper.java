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

package net.legacyfabric.fabric.api.effect;

import net.minecraft.entity.effect.StatusEffect;

import net.legacyfabric.fabric.impl.effect.versioned.PotionHelperImpl;

public interface PotionHelper {
	static void registerLevels(StatusEffect effect, String levels) {
		PotionHelperImpl.registerLevels(effect, levels);
	}

	static void registerAmplifyingFactor(StatusEffect effect, String amplifyingFactor) {
		PotionHelperImpl.registerAmplifyingFactor(effect, amplifyingFactor);
	}
}
