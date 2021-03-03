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

package net.fabricmc.fabric.api.client.itemgroup;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;

public class FabricItemGroupBuilder {
	private final Identifier identifier;
	private Supplier<ItemStack> stackSupplier = () -> new ItemStack((Item) null);
	private Consumer<List<ItemStack>> stacksForDisplay;

	private FabricItemGroupBuilder(Identifier identifier) {
		this.identifier = identifier;
	}

	public static FabricItemGroupBuilder create(Identifier identifier) {
		return new FabricItemGroupBuilder(identifier);
	}

	public FabricItemGroupBuilder icon(Supplier<ItemStack> stackSupplier) {
		this.stackSupplier = stackSupplier;
		return this;
	}

	public FabricItemGroupBuilder appendItems(Consumer<List<ItemStack>> stacksForDisplay) {
		this.stacksForDisplay = stacksForDisplay;
		return this;
	}

	public static ItemGroup build(Identifier identifier, Supplier<ItemStack> iconSupplier) {
		return new FabricItemGroupBuilder(identifier).icon(iconSupplier).build();
	}

	public ItemGroup build() {
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		return new ItemGroup(ItemGroup.itemGroups.length - 1, String.format("%s.%s", identifier.getNamespace(), identifier.getPath())) {
			private ItemStack cachedStack;

			@Override
			public ItemStack getIcon() {
				if (cachedStack != null) {
					return cachedStack;
				}

				return cachedStack = stackSupplier.get();
			}

			@Override
			public Item getIconItem() {
				return getIcon().getItem();
			}

			@Override
			public int getIconMeta() {
				return this.getIcon().getMeta();
			}

			@Environment(EnvType.CLIENT)
			@Override
			public void showItems(List<ItemStack> list) {
				if (stacksForDisplay != null) {
					stacksForDisplay.accept(list);
					return;
				}

				super.showItems(list);
			}
		};
	}
}
