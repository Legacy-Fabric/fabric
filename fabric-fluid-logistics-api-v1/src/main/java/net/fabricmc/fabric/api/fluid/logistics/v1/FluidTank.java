package net.fabricmc.fabric.api.fluid.logistics.v1;

import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

/**
 * Represents a container that can have fluids inserted into
 * and drained out of.
 */
public interface FluidTank extends FluidInsertable, FluidDrainable {
	/**
	 * @return the current fluid container as a block entity.
	 */
	BlockEntity toBlockEntity();

	/**
	 * Transfers a certain amount of fluid in a certain from.
	 *
	 * @param from the from that the fluid should be transferred
	 * @param fluid the fluid that should be transferred
	 * @param amount the amount of fluid that should be transferred
	 * @return whether the transaction was successful or not
	 */
	default boolean transfer(Direction from, FluidBlock fluid, int amount) {
		if (!this.tryExtract(from, fluid, amount, true) && !this.tryInsert(from, fluid, amount, true)) {
			return false;
		}
		this.extract(from, fluid, amount);
		this.insert(from.getOpposite(), fluid, amount);
		return true;
	}
}
