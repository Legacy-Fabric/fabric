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

package net.legacyfabric.fabric.impl.registry.sync.remappers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.compat.BlockCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;

public class BlockRegistryRemapper extends RegistryRemapper<Block> {
	public BlockRegistryRemapper() {
		super(RegistryHelperImpl.registriesGetter.getBlockRegistry(), BLOCKS, "Block", "Blocks");
	}

	private static final boolean hasBlockStateList = VersionUtils.matches(">=1.8");
	private static final boolean hasSpecialCase = VersionUtils.matches(">1.8.9");

	@Override
	public void remap() {
		super.remap();

		if (hasBlockStateList) {
			IdListCompat<BlockState> oldStates = (IdListCompat<BlockState>) Block.BLOCK_STATES;
			IdListCompat<BlockState> newStates = oldStates.createIdList();

			for (Block block : this.registry) {
				if (this.registry.getKey(block).equals(this.toKeyType("tripwire")) && hasSpecialCase) {
					int newBlockId = this.registry.getRawID(block);

					for (int i = 0; i < 15; ++i) {
						int newId = newBlockId << 4 | i;
						BlockState state = block.stateFromData(i);
						int oldId = oldStates.getInt(state);

						if (oldId == -1) {
							LOGGER.info("New block state id %d for block %s", newId, this.registry.getKey(block).toString());
						} else if (oldId != newId) {
							LOGGER.info("Migrating block state id %d of block %s to %d",
									oldId, this.registry.getKey(block).toString(), newId);
						}

						newStates.setValue(state, newId);
					}
				} else {
					for (BlockState blockState : block.getStateManager().getBlockStates()) {
						int newBlockId = this.registry.getRawID(block);
						int newId = newBlockId << 4 | block.getData(blockState);
						int oldId = oldStates.getInt(blockState);

						if (oldId == -1) {
							LOGGER.info("New block state id %d for block %s", newId, this.registry.getKey(block).toString());
						} else if (oldId != newId) {
							LOGGER.info("Migrating block state id %d of block %s to %d",
									oldId, this.registry.getKey(block).toString(), newId);
						}

						newStates.setValue(blockState, newId);
					}
				}
			}

			((BlockCompat) this.registry.getValue(this.toKeyType("air"))).setBLOCK_STATES(newStates);
		}
	}
}
