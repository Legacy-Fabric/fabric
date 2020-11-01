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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

/**
 * Represents a container that can have fluids inserted into
 * and drained out of.
 */
public interface FluidTank extends FluidInsertable, FluidDrainable {
	/**
	 * @return the current fluid container as a block entity.
	 */
	BlockEntity toBlockEntity();

	/**
	 * Transfers a certain amount of fluid in a certain from.
	 *
	 * @param from the from that the fluid should be transferred
	 * @param fluid the fluid that should be transferred
	 * @param amount the amount of fluid that should be transferred
	 * @return whether the transaction was successful or not
	 */
	default boolean transfer(Direction from, FluidBlock fluid, int amount) {
		if (!this.tryExtract(from, fluid, amount, true) && !this.tryInsert(from, fluid, amount, true)) {
			return false;
		}
		this.extract(from, fluid, amount);
		this.insert(from.getOpposite(), fluid, amount);
		return true;
	}
}
