package net.fabricmc.fabric.api.fluid.logistics.v1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;
import net.fabricmc.fabric.api.movingstuff.v1.Instance;
import net.fabricmc.fabric.api.util.NbtType;

/**
 * Provides implementations for abstract methods.
 * This implementation does not check the direction.
 */
public abstract class AbstractFluidTank implements FluidTank {
	private final BlockEntity blockEntity;
	private final List<FluidStack> fluidStacks = Lists.newLinkedList();

	protected AbstractFluidTank(BlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean canExtract(Direction fromSide, FluidBlock fluid, int amount) {
		return (this.getCurrentSingleFill(fromSide, fluid) > amount)
				&& this.toBlockEntity()
				.getWorld()
				.getBlockEntity(
						this.blockEntity.getPos().offset(fromSide)
				)
				instanceof FluidContainerFactory;
	}

	@Override
	public boolean canInsert(Direction fromSide, FluidBlock fluid, int amount) {
		return ((this.getCurrentFill(fromSide) + amount) < this.getMaxCapacity())
				&& this.toBlockEntity()
				.getWorld()
				.getBlockEntity(
						this.blockEntity.getPos().offset(fromSide)
				)
				instanceof FluidContainerFactory;
	}

	@Override
	public Map<FluidUnit, Integer> getCurrentFillMap(Direction fromSide) {
		Map<FluidUnit, Integer> map = new HashMap<>();
		int current = this.getCurrentFill(fromSide);
		return this.getFluidUnitIntegerMap(map, current);
	}

	@Nonnull
	private Map<FluidUnit, Integer> getFluidUnitIntegerMap(Map<FluidUnit, Integer> map, int current) {
		if (current >= 1620) {
			map.put(FluidUnit.BLOCK, 1);
		} else {
			map.put(FluidUnit.BLOCK, 0);

			if (current > 540) {
				int remaining = current % 540;
				map.put(FluidUnit.BOTTLE, (current - remaining) / 540);

				if (remaining != 0) {
					this.putIngotNuggetDropletValues(map, remaining);
				}
			} else {
				map.put(FluidUnit.BOTTLE, 0);

				if (current > 180) {
					this.putIngotNuggetDropletValues(map, current);
				} else {
					map.put(FluidUnit.INGOT, 0);

					if (current > 20) {
						int nuggetRemaining = current % 20;
						map.put(FluidUnit.NUGGET, (nuggetRemaining - current) / 20);

						if (nuggetRemaining != 0) {
							map.put(FluidUnit.DROPLET, nuggetRemaining - current);
						}
					} else {
						map.put(FluidUnit.NUGGET, 0);
						map.put(FluidUnit.DROPLET, current);
					}
				}
			}
		}

		return map;
	}

	@Override
	public void extract(Direction fromSide, FluidBlock fluid, int amount) {
		if ((this.getCurrentFill(fromSide) - amount) < 0 && this.getCurrentSingleFill(fromSide, fluid) > 0) {
			throw new UnsupportedOperationException("Too less fluid!");
		}

		for (Instance<? extends FluidBlock> stack : this.getInstances(fromSide)) {
			if (stack.get().isPresent() && stack.get().get() == fluid){
				stack.setAmount(stack.getAmount() - amount);
				return;
			}
		}
	}

	private <T> void add(List<T> list, T value) {
		list.add(value);
	}

	@Override
	public void insert(Direction fromSide, FluidBlock fluid, int amount) {
		if ((this.getCurrentFill(fromSide) + amount) > this.getMaxCapacity()) {
			throw new UnsupportedOperationException("Too much fluid!");
		} else if (this.getCurrentSingleFill(fromSide, fluid) > 0) {
			for (FluidStack stack : this.getFluidStacks(fromSide)) {
				if (stack.get().isPresent() && stack.get().get() == fluid){
					stack.setAmount(stack.getAmount() + amount);
					return;
				}
			}
		} else {
			this.getFluidStacks(fromSide).add(new FluidStack(fluid, amount));
		}
	}

	@Override
	public Map<FluidUnit, Integer> getCurrentSingleFillMap(Direction fromSide, FluidBlock fluid) {
		Map<FluidUnit, Integer> map = new HashMap<>();
		int current = this.getCurrentSingleFill(fromSide, fluid);
		return this.getFluidUnitIntegerMap(map, current);
	}

	private void putIngotNuggetDropletValues(Map<FluidUnit, Integer> map, int current) {
		int ingotRemaining = current % 180;
		map.put(FluidUnit.INGOT, (current - ingotRemaining) / 180);
		if (ingotRemaining != 0) {
			int nuggetRemaining = ingotRemaining % 20;
			map.put(FluidUnit.NUGGET, (nuggetRemaining - ingotRemaining) / 20);
			if (nuggetRemaining != 0) {
				map.put(FluidUnit.DROPLET, nuggetRemaining - ingotRemaining);
			}
		}
	}

	protected List<FluidStack> getFluidStacks(Direction fromSide) {
		return this.fluidStacks;
	}


	@Override
	public List<? extends Instance<FluidBlock>> getInstances(Direction fromSide) {
		return this.fluidStacks;
	}

	@Override
	public BlockEntity toBlockEntity() {
		return this.blockEntity;
	}

	public void fromTag(CompoundTag tag) {
		ListTag listTag = tag.getList("fluidStacks", NbtType.COMPOUND);
		this.fluidStacks.clear();
		new Iterator<CompoundTag>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return !listTag.isEmpty();
			}

			@Override
			public CompoundTag next() {
				return listTag.getCompound(this.index++);
			}
		}.forEachRemaining(compoundTag -> {
			FluidStack fluidStack = new FluidStack();
			fluidStack.fromTag(compoundTag);
			this.fluidStacks.add(fluidStack);
		});
	}

	public CompoundTag toTag(CompoundTag tag) {
		ListTag listTag = new ListTag();
		for (FluidStack stack : this.fluidStacks) {
			listTag.add(stack.toTag(new CompoundTag()));
		}
		tag.put("fluidStacks", listTag);
		return tag;
	}

	public BlockPos getPos() {
		return this.toBlockEntity().getPos();
	}
}
