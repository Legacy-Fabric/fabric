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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.mixin.registry.sync.BlockAccessor;

public class BlockRegistryRemapper extends RegistryRemapper<Block> {
	public static final IdList<BlockState> OLD_BLOCK_STATES = new IdList<>();
	public static final IdList<BlockState> REMAPPED_BLOCK_STATES = new IdList<>();
	public BlockRegistryRemapper() {
		super(Block.REGISTRY, BLOCKS);
	}

	@Override
	public void remap() {
		for (Block block14 : this.registry) {
			for (BlockState blockState : block14.getStateManager().getBlockStates()) {
				int i = this.registry.getIndex(block14) << 4 | block14.getData(blockState);
				OLD_BLOCK_STATES.set(blockState, i);
			}
		}

		//		IdList<Block> oldList = RegistryHelperImpl.getIdList(this.registry);
		super.remap();

		for (Block block14 : this.registry) {
			for (BlockState blockState : block14.getStateManager().getBlockStates()) {
				int i = this.registry.getIndex(block14) << 4 | block14.getData(blockState);
				REMAPPED_BLOCK_STATES.set(blockState, i);
			}
		}

		//		IdList<Block> newList = RegistryHelperImpl.getIdList(this.registry);
		//		Map<Integer, Integer> old2new = new HashMap<>();
		//
		//		for (Block value : oldList) {
		//			old2new.put(oldList.getId(value), newList.getId(value));
		//		}

		//		for (BlockState blockState : oldStates) {
		//			if (blockState == null) {
		//				LOGGER.warn("Found null block state!");
		//				continue;
		//			}
		//
		//			int id = oldStates.getId(blockState);
		//			int blockId = id >> 4;
		//			int stateId = id & 0xF;
		//			int newBlockId = old2new.get(blockId);
		//			int newBlockStateId = newBlockId << 4 | stateId;
		//
		//			if (id != newBlockStateId) {
		//				LOGGER.info("Remapping block state id from %s to %s", id, newBlockStateId);
		//			}
		//
		//			newStates.set(blockState, newBlockStateId);
		//		}
		//
		//		BlockAccessor.setBLOCK_STATES(newStates);
		BlockAccessor.setBLOCK_STATES(REMAPPED_BLOCK_STATES);
	}
}
