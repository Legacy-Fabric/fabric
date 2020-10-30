package net.fabricmc.fabric.api.fluid.logistics.v1;

/**
 * Represents an object that has access to a fluid container.
 */
@FunctionalInterface
public interface FluidContainerFactory {
	FluidTank getContainer();
}
