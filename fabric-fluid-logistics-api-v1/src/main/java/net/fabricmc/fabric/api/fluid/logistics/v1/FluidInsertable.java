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

import java.util.Map;

import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;
import net.fabricmc.fabric.api.movingstuff.v1.Insertable;

/**
 * An implementation of {@link Insertable} for fluids
 *
 * @see net.fabricmc.fabric.api.fluid.logistics.v1.FluidDrainable
 */
public interface FluidInsertable extends Insertable<FluidBlock, FluidUnit>, DropletAware {
	/**
	 * Inserts a certain amount of fluid into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 */
	@Override
	void insert(Direction fromSide, FluidBlock fluid, int amount);

	/**
	 * Checks whether a certain amount of fluid can be inserted into this insertable
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 * @return whether the insertion is possible
	 */
	@Override
	boolean canInsert(Direction fromSide, FluidBlock fluid, int amount);

	/**
	 * {@inheritDoc}
	 */
	@Override
	Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid);
}
