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

package net.legacyfabric.fabric.impl.block.versioned;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.BLOCKS).register(EarlyInitializer::blockRegistryInit);
	}

	private static void blockRegistryInit(FabricRegistry<?> holder) {
		SyncedFabricRegistry<Block> registry = (SyncedFabricRegistry<Block>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, block) -> {
			if (block.getMaterial() == Material.AIR) {
				block.useNeighbourLight = false;
			} else {
				boolean var12 = false;
				boolean var13 = block.getBlockType() == 10;
				boolean var14 = block instanceof SlabBlock;
				boolean var15 = block == RegistryHelper.getValue(Item.REGISTRY, new Identifier("farmland"));
				boolean var16 = block.transluscent;
				boolean var17 = block.getOpacity() == 0;

				if (var13 || var14 || var15 || var16 || var17) {
					var12 = true;
				}

				block.useNeighbourLight = var12;
			}
		});
	}
}
