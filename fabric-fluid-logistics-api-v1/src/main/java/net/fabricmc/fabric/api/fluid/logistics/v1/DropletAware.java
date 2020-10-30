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

package net.fabricmc.fabric.api.fluid.logistics.v1;

import net.minecraft.block.FluidBlock;

import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;
import net.fabricmc.fabric.api.movingstuff.v1.Aware;

/**
 * Implementation of {@link Aware} for fluids.
 */
public interface DropletAware extends Aware<FluidBlock, FluidUnit> {
	/**
	 * The maximum capacity (in droplets) of a fluid tank is 1620.
	 */
	@Override
	default int getMaxCapacity() {
		return 1620;
	}
}
