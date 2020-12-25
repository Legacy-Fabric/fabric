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

package net.legacyfabric.fabric.test.fluid.volume;

import java.awt.Color;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.minecraft.Bootstrap;
import net.minecraft.block.Blocks;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidHandlers;
import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.settings.FluidSettings;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidStorage;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.base.SimpleFluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

public class FluidTransactionTest {
	@Test
	public void testSingleFluidTransaction() {
		Bootstrap.initialize();
		Fraction max = new Fraction(1000);
		Fraction twenty = new Fraction(20);
		FluidType water = FluidType.getOrCreate(Blocks.WATER, FluidSettings.builder().luminance(0).color(Color.BLUE).build());
		class FluidContainer implements FluidStorage {
			private Fraction stored;
			private final FluidRate fluidRate = SimpleFluidRate.of(twenty, twenty);

			FluidContainer(Fraction stored) {
				this.stored = stored;
			}

			@Override
			public Fraction getStored(FluidType type, Side side) {
				return this.stored;
			}

			@Override
			public void setStored(FluidType type, Side side, Fraction amount) {
				this.stored = amount;
			}

			@Override
			public boolean isValid(FluidType type) {
				return type.equals(water);
			}

			@Override
			public Fraction getMaxFluidVolume(FluidType type, Side side) {
				return max;
			}

			@Override
			public FluidRate getRate() {
				return this.fluidRate;
			}

			@Override
			public void forEach(BiConsumer<FluidType, Fraction> consumer, Side side) {
				consumer.accept(water, this.stored);
			}
		}

		FluidContainer first = new FluidContainer(Fraction.ONE_THOUSAND);
		FluidContainer second = new FluidContainer(Fraction.ZERO);
		FluidHandlers.get(first, water).set(Fraction.ONE_HUNDRED);
		Assertions.assertEquals(FluidHandlers.get(first, water).getStored().longValue(), 100L);
		FluidHandlers.get(first, water).into(FluidHandlers.get(second, water)).move();
		Assertions.assertEquals(80L, FluidHandlers.get(first, water).getStored().longValue());
		Assertions.assertEquals(20L, FluidHandlers.get(second, water).getStored().longValue());
	}
}
