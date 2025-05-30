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

package net.legacyfabric.fabric.mixin.item.group;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.itemgroup.ItemGroup;

import net.legacyfabric.fabric.impl.item.group.ItemGroupExtensions;

@Mixin(ItemGroup.class)
public abstract class MixinItemGroup implements ItemGroupExtensions {
	@Shadow
	@Final
	@Mutable
	public static ItemGroup[] itemGroups;

	@Shadow
	@Final
	private String id;

	@Override
	public void fabric_expandArray() {
		ItemGroup[] tempGroups = itemGroups;
		itemGroups = new ItemGroup[itemGroups.length + 1];
		System.arraycopy(tempGroups, 0, itemGroups, 0, tempGroups.length);
	}

	@Override
	public String getIdentifier() {
		return this.id;
	}
}
