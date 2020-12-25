package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import java.util.function.BiConsumer;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

/**
 * A container that stores fluid.
 */
public interface FluidStorage extends FluidHolder {
	Fraction getStored(FluidType type, Side side);

	void setStored(FluidType type, Side side, Fraction amount);

	boolean isValid(FluidType type);

	void forEach(BiConsumer<FluidType, Fraction> consumer, Side side);
}
