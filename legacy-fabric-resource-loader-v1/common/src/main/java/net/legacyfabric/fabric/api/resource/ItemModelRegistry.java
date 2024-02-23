/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.api.resource;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;
import net.legacyfabric.fabric.impl.resource.loader.ItemModelRegistryImpl;

/**
 * A helper class that allows registering models for items.
 *
 * <p>Older versions of Minecraft do <b>not</b> automatically
 * register items for models. Registering them manually would
 * also not work due to the model identifier's namespace being
 * "minecraft". This class allows registering models to the
 * correct namespace.</p>
 */
@SinceMC("1.8")
public final class ItemModelRegistry {
	public static void registerItemModel(Item item, Identifier modelId) {
		ItemModelRegistryImpl.registerItemModel(item, modelId.toString());
	}

	public static void registerItemModel(Item item, int metadata, Identifier modelId) {
		ItemModelRegistryImpl.registerItemModel(item, metadata, modelId.toString());
	}

	public static void registerBlockItemModel(Block block, Identifier modelId) {
		ItemModelRegistryImpl.registerBlockItemModel(block, modelId.toString());
	}

	public static void registerBlockItemModel(Block block, int metadata, Identifier modelId) {
		ItemModelRegistryImpl.registerBlockItemModel(block, metadata, modelId.toString());
	}
}
