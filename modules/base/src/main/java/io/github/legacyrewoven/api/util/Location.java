/*
 * Copyright (c) 2021 Legacy Rewoven
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package io.github.legacyrewoven.api.util;

import net.minecraft.util.math.Vec3i;

public class Location<T> {
	private final T t;
	private final Vec3i pos;

	public Location(T t, Vec3i pos) {
		this.t = t;
		this.pos = pos;
	}

	public Vec3i getPos() {
		return this.pos;
	}

	public T getT() {
		return this.t;
	}
}
