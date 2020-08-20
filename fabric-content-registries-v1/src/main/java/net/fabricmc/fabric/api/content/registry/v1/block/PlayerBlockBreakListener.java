/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.content.registry.v1.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerBlockBreakListener {
	/**
	 * Determines if a player can break this block and collect its drops.
	 *
	 * @param world The world that the block is being broken in
	 * @param pos The block's position
	 * @param player The player breaking the block.
	 * @return Whether the block should drop anything when broken.
	 */
    boolean canHarvest(World world, BlockPos pos, PlayerEntity player);
}
