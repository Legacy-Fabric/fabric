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
	 * {@inheritDoc}
	 */
	@Override
	Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid);
}
