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

package net.legacyfabric.fabric.api.client.itemgroup;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

import net.legacyfabric.fabric.impl.item.group.ItemGroupExtensions;

public final class FabricItemGroupBuilder {
	private final Identifier identifier;
	private Supplier<Item> itemSupplier = () -> Items.APPLE;
	private BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay;

	private FabricItemGroupBuilder(Identifier identifier) {
		this.identifier = identifier;
	}

	/**
	 * Create a new Item Group Builder.
	 *
	 * @param identifier the id will become the name of the ItemGroup and will be used for the translation key
	 * @return a FabricItemGroupBuilder
	 */
	public static FabricItemGroupBuilder create(Identifier identifier) {
		return new FabricItemGroupBuilder(identifier);
	}

	/**
	 * This is used to add an icon to to the item group.
	 *
	 * @param itemSupplier the supplier should return the item that you wish to show on the tab
	 * @return a reference to the FabricItemGroupBuilder
	 */
	public FabricItemGroupBuilder icon(Supplier<Item> itemSupplier) {
		this.itemSupplier = itemSupplier;
		return this;
	}

	/**
	 * This allows for a custom list of items to be displayed in a tab, this enabled tabs to be created with a custom set of items.
	 *
	 * @param appender Add ItemStack's to this list to show in the ItemGroup
	 * @return a reference to the FabricItemGroupBuilder
	 * @deprecated use {@link FabricItemGroupBuilder#appendItems(Consumer)}
	 */
	@Deprecated
	public FabricItemGroupBuilder stacksForDisplay(Consumer<List<ItemStack>> appender) {
		return appendItems(appender);
	}

	/**
	 * Set the item stacks of this item group, by having the consumer add them to the passed list.
	 * This bypasses {@link Item#appendItemStacks(Item, ItemGroup, List)}. If you want to append stacks from your items, consider using {@linkplain #appendItems(BiConsumer) the other overload}.
	 *
	 * @param stacksForDisplay Add ItemStack's to this list to show in the ItemGroup
	 * @return a reference to the FabricItemGroupBuilder
	 */
	public FabricItemGroupBuilder appendItems(Consumer<List<ItemStack>> stacksForDisplay) {
		return appendItems((itemStacks, itemGroup) -> stacksForDisplay.accept(itemStacks));
	}

	/**
	 * Set the item stacks of this item group, by having the consumer add them to the passed list.
	 * Compared to the other overload, this one also passes the new ItemGroup.
	 * This allows you to call {@link Item#appendItemStacks(Item, ItemGroup, List)} yourself if you want.
	 *
	 * @param stacksForDisplay Add ItemStack's to this list to show in the ItemGroup, and check if the item is in the ItemGroup
	 * @return a reference to the FabricItemGroupBuilder
	 */
	public FabricItemGroupBuilder appendItems(BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay) {
		this.stacksForDisplay = stacksForDisplay;
		return this;
	}

	/**
	 * This is a single method that makes creating an ItemGroup with an icon one call.
	 *
	 * @param identifier    the id will become the name of the ItemGroup and will be used for the translation key
	 * @param stackSupplier the supplier should return the item that you wish to show on the tab
	 * @return An instance of the built ItemGroup
	 */
	public static ItemGroup build(Identifier identifier, Supplier<Item> stackSupplier) {
		return new FabricItemGroupBuilder(identifier).icon(stackSupplier).build();
	}

	/**
	 * Create an instance of the ItemGroup.
	 *
	 * @return An instance of the built ItemGroup
	 */
	public ItemGroup build() {
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		return new ItemGroup(ItemGroup.itemGroups.length - 1, String.format("%s.%s", identifier.getNamespace(), identifier.getPath())) {
			@Override
			public Item getIconItem() {
				return itemSupplier.get();
			}

			@Override
			public void showItems(List<ItemStack> stacks) {
				if (stacksForDisplay != null) {
					stacksForDisplay.accept(stacks, this);
					return;
				}

				super.showItems(stacks);
			}
		};
	}
}
