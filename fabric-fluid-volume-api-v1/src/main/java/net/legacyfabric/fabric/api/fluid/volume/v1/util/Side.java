package net.legacyfabric.fabric.api.fluid.volume.v1.util;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.util.math.Direction;

public enum Side {
	DOWN,
	UP,
	NORTH,
	SOUTH,
	WEST,
	EAST,
	UNKNOWN;

	private static final Side[] VALUES = values();

	@Nonnull
	public static Side fromDirection(Direction d){
		if(d == null){
			return UNKNOWN;
		}
		return VALUES[d.ordinal()];
	}

	public Optional<Direction> toDirection() {
		return Optional.of(this).map(side -> side == UNKNOWN ? null : side).map(side -> Direction.values()[side.ordinal()]);
	}
}
