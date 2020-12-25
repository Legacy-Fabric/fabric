/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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
