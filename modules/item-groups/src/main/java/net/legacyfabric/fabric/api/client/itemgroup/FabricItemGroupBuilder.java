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

import java.util.function.Supplier;

import net.legacyfabric.fabric.impl.item.group.ItemGroupExtensions;

import net.minecraft.item.Item;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

public class FabricItemGroupBuilder {
	private Identifier identifier;
	private Supplier<Item> icon;

	private FabricItemGroupBuilder(Identifier identifier) {
		this.identifier = identifier;
	}

	public static FabricItemGroupBuilder create(Identifier identifier) {
		return new FabricItemGroupBuilder(identifier);
	}

	public FabricItemGroupBuilder identifier(Identifier identifier) {
		this.identifier = identifier;
		return this;
	}

	public FabricItemGroupBuilder icon(Supplier<Item> icon) {
		this.icon = icon;
		return this;
	}

	public ItemGroup build() {
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		int index = ItemGroup.itemGroups.length - 1;
		return ItemGroup.itemGroups[index] = new ItemGroup(index, String.format("%s.%s", identifier.getNamespace(), identifier.getPath())) {
			@Override
			public Item getIconItem() {
				return icon.get();
			}
		};
	}
}
