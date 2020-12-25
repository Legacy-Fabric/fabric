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

package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

public interface FluidHolder {
	/**
	 * @return Returns the maximum amount of fluid that can be stored
	 */
	Fraction getMaxFluidVolume(FluidType type, Side side);

	/**
	 * @return Returns the rate of transfer of this fluid holder
	 */
	FluidRate getRate();

	/**
	 * @param side The direction
	 * @return The maximum input for the specified direction
	 */
	default Fraction getMaxInput(FluidType type, Side side) {
		return this.getRate().getMaxInput(type, side);
	}

	/**
	 * @param side The direction
	 * @return The maximum output for the specified direction
	 */
	default Fraction getMaxOutput(FluidType type, Side side) {
		return this.getRate().getMaxOutput(type, side);
	}
}
