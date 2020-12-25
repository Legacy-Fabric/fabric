package net.legacyfabric.fabric.test.fluid.volume;

import java.awt.Color;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import net.minecraft.block.Blocks;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidHandlers;
import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.settings.FluidSettings;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidStorage;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.base.SimpleFluidRate;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluidTransactionTest {
	@Test
	public void testSingleFluidTransaction() {
		Fraction max = new Fraction(1000);
		Fraction twenty = new Fraction(20);
		FluidType water = FluidType.getOrCreate(Blocks.WATER, FluidSettings.builder().luminance(0).color(Color.BLUE).build());
		class FluidContainer implements FluidStorage {
			private Fraction stored;
			private final FluidRate fluidRate = SimpleFluidRate.of(twenty, twenty);

			public FluidContainer(Fraction stored) {
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
		assertEquals(FluidHandlers.get(first, water).getStored().longValue(), 100L);
		long moveStart = System.nanoTime();
		FluidHandlers.get(first, water).into(FluidHandlers.get(second, water)).move();
		long moveEnd = System.nanoTime() - moveStart;
		System.out.println("Moving: " + (moveEnd / 1_000_000D));
		assertEquals(80L, FluidHandlers.get(first, water).getStored().longValue());
		assertEquals(20L, FluidHandlers.get(second, water).getStored().longValue());
	}
}
