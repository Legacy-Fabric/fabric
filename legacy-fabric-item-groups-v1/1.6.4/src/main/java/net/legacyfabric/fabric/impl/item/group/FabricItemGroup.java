/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.impl.item.group;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FabricItemGroup extends ItemGroup {
	private final Supplier<ItemStack> itemSupplier;
	private final BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay;

	public FabricItemGroup(int index, String id, Supplier<ItemStack> itemSupplier, BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay) {
		super(index, id);
		this.itemSupplier = itemSupplier;
		this.stacksForDisplay = stacksForDisplay;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Item method_3320() {
		return this.itemSupplier.get().getItem();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void showItems(List stacks) {
		if (stacksForDisplay != null) {
			stacksForDisplay.accept(stacks, this);
			return;
		}

		super.showItems(stacks);
	}
}
