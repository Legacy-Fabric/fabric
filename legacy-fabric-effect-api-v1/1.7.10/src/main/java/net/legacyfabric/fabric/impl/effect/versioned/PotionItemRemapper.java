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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.PotionItem;

import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.mixin.effect.PotionItemAccessor;

public class PotionItemRemapper implements RegistryRemapCallback<StatusEffect> {
	public static final List<PotionItem> POTION_ITEMS = new ArrayList<>();

	@Override
	public void callback(Map<Integer, RegistryEntry<StatusEffect>> changedIdsMap) {
		for (PotionItem potionItem : POTION_ITEMS) {
			((PotionItemAccessor) potionItem).getField_4383().clear();
		}

		PotionItemAccessor.getSTATUS_EFFECTS_2().clear();
	}
}
