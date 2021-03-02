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

package net.fabricmc.fabric.test.client.item.group;

import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

@Environment(EnvType.CLIENT)
public class ItemGroupTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		IntStream.range(0, 25).forEach(i -> FabricItemGroupBuilder.create(new Identifier("fabric-item-groups-v0-testmod:test_itemgroup_" + i)).appendItems(list -> Items.DYE.appendItemStacks(Items.DYE, null, list)).icon(() -> new ItemStack(Block.getById(i + 1))).build());
	}
}
