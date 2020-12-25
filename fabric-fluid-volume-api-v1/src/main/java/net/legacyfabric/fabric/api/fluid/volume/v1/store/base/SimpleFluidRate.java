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

package net.legacyfabric.fabric.api.fluid.volume.v1.store.base;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

/**
 * A {@link FluidRate} implementation that uses static values.
 */
public class SimpleFluidRate implements FluidRate {
	private final Fraction input;
	private final Fraction output;

	private SimpleFluidRate(Fraction input, Fraction output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public Fraction getMaxInput(FluidType type, Side side) {
		return this.input;
	}

	@Override
	public Fraction getMaxOutput(FluidType type, Side side) {
		return this.output;
	}

	public static SimpleFluidRate of(long input, long output) {
		return new SimpleFluidRate(Fraction.of(input), Fraction.of(output));
	}

	public static SimpleFluidRate of(Fraction input, Fraction output) {
		return new SimpleFluidRate(input, output);
	}

	public static SimpleFluidRate of(Number input, Number output) {
		return of(input.longValue(), output.longValue());
	}
}
