package net.fabricmc.fabric.api.command.v2;

import net.minecraft.util.math.BlockPos;

public class Location<T> {
	private final T t;
	private BlockPos pos;

	public Location(T t, BlockPos pos) {
		this.t = t;
		this.pos = pos;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public T getT() {
		return this.t;
	}
}
