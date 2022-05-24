/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.api.itemgroup.v0;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;

public abstract class FabricItemGroup extends ItemGroup {
	protected final List<ItemStack> stacks;

	protected FabricItemGroup(int index, String id, List<ItemStack> stacks) {
		super(index, id);
		this.stacks = stacks;
	}

	/**
	 * Add item stack for display.
	 *
	 * @param stack the item stack for display
	 */
	public void addStack(ItemStack stack) {
		this.stacks.add(stack);
	}

	/**
	 * Remove displayed item stack.
	 *
	 * @param stack the displayed item stack
	 * @return true if removed else false
	 */
	public boolean removeStack(ItemStack stack) {
		return this.stacks.remove(stack);
	}

	@Override
	public void showItems(List stacks) {
		super.showItems(stacks);
		stacks.addAll(this.stacks);
	}
}
