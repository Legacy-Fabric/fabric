package net.fabricmc.fabric.api.fluid.logistics.v1;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Represents an object that has access to a fluid container.
 */
@FunctionalInterface
public interface FluidContainerFactory {
	FluidContainer getContainer(BlockState state, World world, BlockPos pos);
}
