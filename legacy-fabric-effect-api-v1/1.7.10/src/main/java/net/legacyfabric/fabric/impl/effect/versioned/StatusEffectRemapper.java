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

import java.util.Map;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;

import net.minecraft.entity.effect.StatusEffect;

import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.mixin.effect.StatusEffectAccessor;

public class StatusEffectRemapper implements RegistryRemapCallback<StatusEffect> {
	@Override
	public void callback(Map<Integer, FabricRegistryEntry<StatusEffect>> changedIdsMap) {
		for (FabricRegistryEntry<StatusEffect> entry : changedIdsMap.values()) {
			StatusEffect effect = entry.getValue();

			if (effect != null) {
				((StatusEffectAccessor) effect).setId(entry.getId());
			}
		}
	}
}
