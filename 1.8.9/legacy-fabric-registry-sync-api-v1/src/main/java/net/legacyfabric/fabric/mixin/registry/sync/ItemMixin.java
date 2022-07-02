/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.ItemCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

@Mixin(Item.class)
public class ItemMixin implements ItemCompat {
	@Shadow
	@Final
	private static Map<Block, Item> BLOCK_ITEMS;

	@Shadow
	@Final
	public static SimpleRegistry<net.minecraft.util.Identifier, Item> REGISTRY;

	@Override
	public Map<Block, Item> getBLOCK_ITEMS() {
		return BLOCK_ITEMS;
	}

	@Override
	public void addToRegistry(int id, Identifier identifier, Item item) {
		REGISTRY.add(id, new net.minecraft.util.Identifier(identifier.toString()), item);
	}

	@Override
	public <K> SimpleRegistryCompat<K, Item> getRegistry() {
		return (SimpleRegistryCompat<K, Item>) REGISTRY;
	}
}
