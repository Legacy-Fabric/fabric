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

package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.impl.registry.sync.compat.BlockCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;

@Mixin(Block.class)
public class BlockMixin implements BlockCompat {
	@Mutable
	@Shadow
	@Final
	public static IdList BLOCK_STATES;

	@Override
	public void setBLOCK_STATES(IdListCompat<BlockState> block_states) {
		BLOCK_STATES = (IdList) block_states;
	}
}
