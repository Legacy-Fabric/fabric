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

package net.legacyfabric.fabric.impl.effect.versioned;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.effect.StatusEffect;

import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.mixin.effect.StatusEffectStringsAccessor;

public class StatusEffectStringsRemapper implements RegistryRemapCallback<StatusEffect> {
	@Override
	public void callback(Map<Integer, RegistryEntry<StatusEffect>> changedIdsMap) {
		Map<Integer, String> firstStringMap = new HashMap<>();

		for (Map.Entry<Integer, String> entry : StatusEffectStringsAccessor.getField_9162().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			firstStringMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setField_9162(firstStringMap);

		Map<Integer, String> secondStringMap = new HashMap<>();

		for (Map.Entry<Integer, String> entry : StatusEffectStringsAccessor.getField_9163().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			secondStringMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setField_9163(secondStringMap);

		Map<Integer, Integer> integerMap = new HashMap<>();

		for (Map.Entry<Integer, Integer> entry : StatusEffectStringsAccessor.getField_9164().entrySet()) {
			int id = entry.getKey();

			if (changedIdsMap.containsKey(id)) {
				id = changedIdsMap.get(id).getId();
			}

			integerMap.put(id, entry.getValue());
		}

		StatusEffectStringsAccessor.setField_9164(integerMap);
	}
}
