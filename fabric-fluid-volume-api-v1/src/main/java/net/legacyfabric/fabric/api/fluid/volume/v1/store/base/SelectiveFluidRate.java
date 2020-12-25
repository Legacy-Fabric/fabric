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

import java.util.function.Function;
import java.util.function.ToLongFunction;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

/**
 * A {@link FluidRate} implementation that dynamically gets the rate depending on the {@link FluidType}.
 */
public class SelectiveFluidRate implements FluidRate {
	private final Function<FluidType, Fraction> input;
	private final Function<FluidType, Fraction> output;

	public SelectiveFluidRate(Function<FluidType, Fraction> input, Function<FluidType, Fraction> output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public Fraction getMaxInput(FluidType type, Side side) {
		return this.input.apply(type);
	}

	@Override
	public Fraction getMaxOutput(FluidType type, Side side) {
		return this.output.apply(type);
	}

	public static SelectiveFluidRate of(Function<FluidType, Fraction> input, Function<FluidType, Fraction> output) {
		return new SelectiveFluidRate(input, output);
	}

	public static SelectiveFluidRate of(ToLongFunction<FluidType> input, ToLongFunction<FluidType> output) {
		return new SelectiveFluidRate((type) -> Fraction.of(input.applyAsLong(type)), (type) -> Fraction.of(output.applyAsLong(type)));
	}
}
