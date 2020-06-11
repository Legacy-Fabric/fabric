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
