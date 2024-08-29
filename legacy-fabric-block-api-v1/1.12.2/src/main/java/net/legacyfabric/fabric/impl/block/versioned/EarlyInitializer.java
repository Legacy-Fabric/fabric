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
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.mixin.block.versioned.ItemAccessor;

public class EarlyInitializer implements PreLaunchEntrypoint {
	private static final boolean checkGrass = VersionUtils.matches(">1.8.9");
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.BLOCKS).register(EarlyInitializer::blockRegistryInit);
		RegistryInitializedEvent.event(RegistryIds.ITEMS).register(EarlyInitializer::itemRegistryInit);
	}

	private static void blockRegistryInit(Registry<?> holder) {
		SyncedRegistry<Block> registry = (SyncedRegistry<Block>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, block) -> {
			for (BlockState blockState : block.getStateManager().getBlockStates()) {
				int i = rawId << 4 | block.getData(blockState);
				Block.BLOCK_STATES.set(blockState, i);
			}
		});

		registry.fabric$getRegistryRemapCallback().register(new BlockStateRemapper());

		registry.fabric$getEntryAddedCallback().register((rawId, id, block) -> {
			if (block.material == Material.AIR) {
				block.useNeighbourLight = false;
			} else {
				boolean var12 = false;
				boolean var13 = block instanceof StairsBlock;
				boolean var14 = block instanceof SlabBlock;
				boolean var15 = block == RegistryHelper.getValue(Item.REGISTRY, new Identifier("farmland"))
						|| (checkGrass && block == RegistryHelper.getValue(Item.REGISTRY, new Identifier("grass_path")));
				boolean var16 = block.transluscent;
				boolean var17 = block.opacity == 0;

				if (var13 || var14 || var15 || var16 || var17) {
					var12 = true;
				}

				block.useNeighbourLight = var12;
			}
		});
	}

	private static void itemRegistryInit(Registry<?> holder) {
		SyncedRegistry<Item> registry = (SyncedRegistry<Item>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, item) -> {
			if (item instanceof BlockItem) {
				ItemAccessor.getBLOCK_ITEMS().put(((BlockItem) item).getBlock(), item);
			}
		});
	}
}
