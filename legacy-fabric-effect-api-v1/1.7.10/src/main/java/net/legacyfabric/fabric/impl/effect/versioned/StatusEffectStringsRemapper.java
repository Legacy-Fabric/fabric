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

package net.legacyfabric.fabric.impl.effect.versioned;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.mixin.effect.StatusEffectStringsAccessor;

public class StatusEffectStringsRemapper implements RegistryRemapCallback<StatusEffect> {
	@Override
	public void callback(Map<Integer, FabricRegistryEntry<StatusEffect>> changedIdsMap) {
		HashMap<Integer, String> levelsMap = new HashMap<>();

		for (Map.Entry<Integer, String> entry : StatusEffectStringsAccessor.getLevelsMap().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			levelsMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setLevelsMap(levelsMap);

		HashMap<Integer, String> amplifyingFactorsMap = new HashMap<>();

		for (Map.Entry<Integer, String> entry : StatusEffectStringsAccessor.getAmplifyingFactorsMap().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			amplifyingFactorsMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setAmplifyingFactorsMap(amplifyingFactorsMap);

		HashMap<Integer, Integer> colorsMap = new HashMap<>();

		for (Map.Entry<Integer, Integer> entry : StatusEffectStringsAccessor.getEffectColorsMap().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			colorsMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setEffectColorsMap(colorsMap);
	}
}
