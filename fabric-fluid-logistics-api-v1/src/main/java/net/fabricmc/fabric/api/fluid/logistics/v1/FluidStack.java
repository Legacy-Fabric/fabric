/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.fluid.logistics.v1;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.movingstuff.v1.Instance;

/**
 * An implementation of {@link Instance} for {@link FluidBlock}s.
 *
 * @see net.minecraft.block.FluidBlock
 */
public class FluidStack implements Instance<FluidBlock> {
	/**
	 * Since 1.8.9 has no concept of fluids, this value may be nullable.
	 */
	@Nullable
	private FluidBlock fluidBlock;
	private int amount;
	@Nonnull
	private CompoundTag tag;

	public FluidStack(@Nullable FluidBlock fluidBlock, int amount, @Nonnull CompoundTag tag) {
		this.fluidBlock = fluidBlock;
		this.amount = amount;
		this.tag = tag;
	}

	public FluidStack(FluidBlock fluidBlock, int amount) {
		this(fluidBlock, amount, new CompoundTag());
	}

	public FluidStack(FluidBlock fluidBlock) {
		this(fluidBlock, 1, new CompoundTag());
	}

	@Nonnull
	@Override
	public Optional<FluidBlock> get() {
		return Optional.ofNullable(this.fluidBlock);
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Nonnull
	@Override
	public CompoundTag getTag() {
		return this.tag;
	}

	@Override
	public void setTag(@Nonnull CompoundTag tag) {
		this.tag = tag;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (this.get().isPresent()){
			tag.putString("id", Block.REGISTRY.getIdentifier(this.get().get()).toString());
		} else {
			tag.putString("id", "empty");
		}
		tag.put("tag", this.tag);
		tag.putInt("amount", this.getAmount());
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		String id = tag.getString("id");
		if (!id.equals("empty")) {
			this.fluidBlock = (FluidBlock) Block.REGISTRY.get(new Identifier(id));
		}
		this.setAmount(tag.getInt("amount"));
		this.setTag(tag.getCompound("tag"));
	}
}
