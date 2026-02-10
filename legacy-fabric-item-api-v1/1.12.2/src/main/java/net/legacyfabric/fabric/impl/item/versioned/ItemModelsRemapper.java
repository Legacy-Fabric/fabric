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

package net.legacyfabric.fabric.impl.item.versioned;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.item.ItemModelShaper;
import net.minecraft.client.resource.ModelIdentifier;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.mixin.item.versioned.ItemModelsAccessor;

public class ItemModelsRemapper implements RegistryRemapCallback<Item> {
	private static final Map<Identifier, Map<Integer, ModelIdentifier>> modelIds = new HashMap<>();

	public static void registerModelId(Identifier id, int metadata, ModelIdentifier modelId) {
		modelIds.computeIfAbsent(id, k -> new HashMap<>())
				.put(metadata, modelId);
	}

	private int pack(int itemId, int metadata) {
		return itemId << 16 | metadata;
	}

	private ItemModelShaper getModelRegistry() {
		return Minecraft.getInstance().getItemRenderer().getModelShaper();
	}

	@Override
	public void callback(Map<Integer, FabricRegistryEntry<Item>> changedIdsMap) {
		((ItemModelsAccessor) getModelRegistry()).getModelIds().clear();

		for (Map.Entry<Identifier, Map<Integer, ModelIdentifier>> entry : modelIds.entrySet()) {
			Identifier itemId = entry.getKey();

			Item item = RegistryHelper.<Item>getValue(Item.REGISTRY, itemId);
			int itemIndex = RegistryHelper.getRawId(Item.REGISTRY, item);

			if (changedIdsMap.containsKey(itemIndex)) {
				itemIndex = changedIdsMap.get(itemIndex).getId();
			}

			for (Map.Entry<Integer, ModelIdentifier> modelIdEntry : entry.getValue().entrySet()) {
				((ItemModelsAccessor) getModelRegistry()).getModelIds()
						.put(pack(itemIndex, modelIdEntry.getKey()), modelIdEntry.getValue());
			}
		}

		getModelRegistry().rebuildCache();
	}
}
