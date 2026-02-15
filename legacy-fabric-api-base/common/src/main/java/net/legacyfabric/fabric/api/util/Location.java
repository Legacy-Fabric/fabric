/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import net.legacyfabric.fabric.mixin.base.Vec3iAccessor;

public class Location<T> {
	private final T t;
	private final BlockPos pos;

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

	/* Backward compatibility for Legacy Fabric intermediary where BlockPos and Vec3i are swapped before 1.8 */
	public Location(T t, Vec3i pos) {
		this(t, new BlockPos(((Vec3iAccessor) pos).getX(), ((Vec3iAccessor) pos).getY(), ((Vec3iAccessor) pos).getZ()));
	}

	public Vec3i getPosAsVec3i() {
		return new Vec3i(this.pos.getX(), this.pos.getY(), this.pos.getZ());
	}
}
