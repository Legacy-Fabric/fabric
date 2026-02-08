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

package net.legacyfabric.fabric.impl.block.versioned;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.mixin.block.versioned.BlockAccessor;

public class BlockStateRemapper implements RegistryRemapCallback<Block> {

	@Override
	public void callback(Map<Integer, FabricRegistryEntry<Block>> changedIdsMap) {
		IdsHolder<BlockState> newList = Block.STATE_REGISTRY.fabric$new();

		for (Block block : Block.REGISTRY) {
			int blockRawId = RegistryHelper.getRawId(Block.REGISTRY, block);

			if (changedIdsMap.containsKey(blockRawId)) {
				blockRawId = changedIdsMap.get(blockRawId).getId();
			}

			Identifier blockId = RegistryHelper.getId(Block.REGISTRY, block);

			for (BlockState state : block.stateDefinition().all()) {
				int blockStateId = blockRawId << 4 | block.getMetadataFromState(state);

				newList.fabric$setValue(state, blockStateId);
			}
		}

		BlockAccessor.setBlockStateList((CrudeIncrementalIntIdentityHashMap<BlockState>) newList);
	}
}
