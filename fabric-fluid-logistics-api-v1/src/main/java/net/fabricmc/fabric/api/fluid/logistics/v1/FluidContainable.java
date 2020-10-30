package net.fabricmc.fabric.api.fluid.logistics.v1;

import net.minecraft.item.ItemStack;

/**
 * Represents an item that stores fluids.
 */
public interface FluidContainable {
	FluidStack asFluidStack(ItemStack stack);
}
