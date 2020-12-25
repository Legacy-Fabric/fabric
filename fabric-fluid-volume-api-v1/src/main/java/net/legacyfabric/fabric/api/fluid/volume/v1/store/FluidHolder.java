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
