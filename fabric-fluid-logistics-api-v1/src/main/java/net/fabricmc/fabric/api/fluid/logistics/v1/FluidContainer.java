package net.fabricmc.fabric.api.fluid.logistics.v1;

import net.minecraft.block.entity.BlockEntity;

/**
 * Represents a container that can have fluids inserted into
 * and drained out of.
 */
public interface FluidContainer extends FluidInsertable, FluidDrainable {
	/**
	 * @return the current fluid container as a block entity.
	 */
	BlockEntity toBlockEntity();
}
