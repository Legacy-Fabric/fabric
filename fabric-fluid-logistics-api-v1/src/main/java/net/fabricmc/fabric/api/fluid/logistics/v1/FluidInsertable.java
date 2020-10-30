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
 * @see net.fabricmc.fabric.api.movingstuff.v1.Aware
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
	 * Inserts a certain amount of fluid into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 * @param unit the unit
	 */
	@Override
	default void insert(Direction fromSide, FluidBlock fluid, int amount, FluidUnit unit) {
		Insertable.super.insert(fromSide, fluid, amount, unit);
	}

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
	 * Checks whether a certain amount of fluid can be inserted into this insertable
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 * @param unit the unit
	 * @return whether the insertion is possible
	 */
	@Override
	default boolean canInsert(Direction fromSide, FluidBlock fluid, int amount, FluidUnit unit) {
		return Insertable.super.canInsert(fromSide, fluid, amount, unit);
	}

	/**
	 * Attempt to insert fluid into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return whether the insertion was/would be successful.
	 */
	@Override
	default boolean tryInsert(Direction fromSide, FluidBlock fluid, int amount, boolean simulate) {
		return Insertable.super.tryInsert(fromSide, fluid, amount, simulate);
	}

	/**
	 * Attempt to insert fluid into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param amount how much fluid to insert.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @param unit the unit
	 * @return whether the insertion was/would be successful.
	 */
	@Override
	default boolean tryInsert(Direction fromSide, FluidBlock fluid, int amount, boolean simulate, FluidUnit unit) {
		return Insertable.super.tryInsert(fromSide, fluid, amount, simulate, unit);
	}

	/**
	 * Attempt to insert fluid, only filling partially if the container can't hold all the fluid.
	 *
	 * @param fromSide the side from which to insert.
	 * @param fluid the type of fluid to insert.
	 * @param maxAmount how much fluid to insert at maximum.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return an integer amount of how much fluid was/would be moved.
	 */
	@Override
	default int tryPartialInsert(Direction fromSide, FluidBlock fluid, int maxAmount, boolean simulate) {
		return Insertable.super.tryPartialInsert(fromSide, fluid, maxAmount, simulate);
	}

	/**
	 * Gets the total number of instances of a certain fluid
	 *
	 * @param fromSide the direction
	 * @param fluid the fluid
	 * @return the total number of instances of a certain fluid
	 */
	@Override
	default int getCurrentSingleFill(Direction fromSide, FluidBlock fluid) {
		return Insertable.super.getCurrentSingleFill(fromSide, fluid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid);
}
