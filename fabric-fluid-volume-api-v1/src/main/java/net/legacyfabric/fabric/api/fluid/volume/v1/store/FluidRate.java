package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

/**
 * Specifies rates of fluid transfer.
 */
public interface FluidRate {
	Fraction getMaxInput(FluidType type, Side side);

	Fraction getMaxOutput(FluidType type, Side side);
}
