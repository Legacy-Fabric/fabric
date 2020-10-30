package net.fabricmc.fabric.api.fluid.logistics.v1;

import java.util.Map;

import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;
import net.fabricmc.fabric.api.movingstuff.v1.Extractable;
import net.fabricmc.fabric.api.movingstuff.v1.Insertable;

/**
 * An implementation of {@link Insertable} for fluids
 *
 * @see net.fabricmc.fabric.api.fluid.logistics.v1.FluidInsertable
 */
public interface FluidDrainable extends Extractable<FluidBlock, FluidUnit>, DropletAware {
	/**
	 * Extracts a certain amount of fluid into from extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 */
	@Override
	void extract(Direction fromSide, FluidBlock fluid, int amount);


	/**
	 * Extracts a certain amount of fluid into from extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 * @param unit the unit
	 */
	@Override
	default void extract(Direction fromSide, FluidBlock fluid, int amount, FluidUnit unit) {
		Extractable.super.extract(fromSide, fluid, amount, unit);
	}

	/**
	 * Checks whether a certain amount of fluid can be extracted from this extractable
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 * @return whether the extraction is possible
	 */
	@Override
	boolean canExtract(Direction fromSide, FluidBlock fluid, int amount);

	/**
	 * Checks whether a certain amount of fluid can be extracted from this extractable
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 * @param unit the unit
	 * @return whether the extraction is possible
	 */
	@Override
	default boolean canExtract(Direction fromSide, FluidBlock fluid, int amount, FluidUnit unit) {
		return Extractable.super.canExtract(fromSide, fluid, amount, unit);
	}

	/**
	 * Attempts to extract fluid from this extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return whether the extraction was/would be successful.
	 */
	@Override
	default boolean tryExtract(Direction fromSide, FluidBlock fluid, int amount, boolean simulate) {
		return Extractable.super.tryExtract(fromSide, fluid, amount, simulate);
	}

	/**
	 * Attempts to extract fluid from this extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param amount how much fluid to extract.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @param unit the unit
	 * @return whether the extraction was/would be successful.
	 */
	@Override
	default boolean tryExtract(Direction fromSide, FluidBlock fluid, int amount, boolean simulate, FluidUnit unit) {
		return Extractable.super.tryExtract(fromSide, fluid, amount, simulate, unit);
	}

	/**
	 * Attempt to extract fluid, only draining partially if the container can't hold all the fluid.
	 *
	 * @param fromSide the side from which to extract.
	 * @param fluid the type of fluid to extract.
	 * @param maxAmount how much fluid to extract at maximum.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return an integer amount of how much fluid was/would be moved.
	 */
	@Override
	default int tryPartialExtract(Direction fromSide, FluidBlock fluid, int maxAmount, boolean simulate) {
		return Extractable.super.tryPartialExtract(fromSide, fluid, maxAmount, simulate);
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
		return Extractable.super.getCurrentSingleFill(fromSide, fluid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid);
}
