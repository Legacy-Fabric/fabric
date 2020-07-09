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

package net.fabricmc.fabric.impl.util;

import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BlockHitResult extends HitResult {
	private final Direction side;
	private final BlockPos blockPos;
	private final boolean missed;
	private final boolean insideBlock;

	public static BlockHitResult createMissed(Vec3d pos, Direction side, BlockPos blockPos) {
		return new BlockHitResult(true, pos, side, blockPos, false);
	}

	public BlockHitResult(Vec3d pos, Direction side, BlockPos blockPos, boolean bl) {
		this(false, pos, side, blockPos, bl);
	}

	private BlockHitResult(boolean missed, Vec3d pos, Direction side, BlockPos blockPos, boolean insideBlock) {
		super(pos, side);
		this.missed = missed;
		this.side = side;
		this.blockPos = blockPos;
		this.insideBlock = insideBlock;
	}

	public BlockHitResult withSide(Direction side) {
		return new BlockHitResult(this.missed, this.pos, side, this.blockPos, this.insideBlock);
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	public Direction getSide() {
		return this.side;
	}

	public Type getType() {
		return this.missed ? Type.MISS : Type.BLOCK;
	}

	public boolean isInsideBlock() {
		return this.insideBlock;
	}
}
