/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.impl.registry.sync;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.mixin.registry.sync.BlockAccessor;

public class BlockRegistryRemapper extends RegistryRemapper<Block> {
	public BlockRegistryRemapper() {
		super(Block.REGISTRY, BLOCKS);
	}

	@Override
	public void remap() {
		IdList<Block> oldList = RegistryHelperImpl.getIdList(this.registry);
		super.remap();
		IdList<Block> newList = RegistryHelperImpl.getIdList(this.registry);
		Map<Integer, Integer> old2new = new HashMap<>();

		for (Block value : oldList) {
			old2new.put(oldList.getId(value), newList.getId(value));
		}

		IdList<BlockState> oldStates = Block.BLOCK_STATES;
		IdList<BlockState> newStates = new IdList<>();

		for (BlockState blockState : oldStates) {
			int id = oldStates.getId(blockState);
			int blockId = id >> 4;
			int stateId = id & 0xF;
			int newBlockId = old2new.get(blockId);
			int newBlockStateId = newBlockId << 4 | stateId;
			newStates.set(blockState, newBlockStateId);
		}

		BlockAccessor.setBLOCK_STATES(newStates);
	}
}
