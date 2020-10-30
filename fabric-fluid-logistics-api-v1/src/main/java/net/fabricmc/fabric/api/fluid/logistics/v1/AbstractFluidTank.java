package net.fabricmc.fabric.api.fluid.logistics.v1;

import java.util.Map;

import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;

/**
 * Provides implementations for abstract methods.
 */
public abstract class AbstractFluidTank implements FluidTank {
	private final BlockEntity blockEntity;

	protected AbstractFluidTank(BlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean canExtract(Direction fromSide, FluidBlock fluid, int amount) {
		return this.toBlockEntity().getWorld().getBlockEntity(this.blockEntity.getPos().offset(fromSide)) instanceof FluidContainerFactory;
	}

	@Override
	public boolean canInsert(Direction fromSide, FluidBlock fluid, int amount) {
		return this.toBlockEntity().getWorld().getBlockEntity(this.blockEntity.getPos().offset(fromSide)) instanceof FluidContainerFactory;
	}

	@Override
	public Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid) {
		return null;
	}

	@Override
	public BlockEntity toBlockEntity() {
		return this.blockEntity;
	}
}
