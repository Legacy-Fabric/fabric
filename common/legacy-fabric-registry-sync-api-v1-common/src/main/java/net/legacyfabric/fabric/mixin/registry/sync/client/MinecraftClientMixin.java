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

package net.legacyfabric.fabric.mixin.registry.sync.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.registry.sync.BlockRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.ItemRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements RegistryRemapperAccess {
	@Unique
	private final RegistryRemapper<Item> itemRemapper = new ItemRegistryRemapper();
	@Unique
	private final RegistryRemapper<Block> blockRemapper = new BlockRegistryRemapper();

	@Override
	public RegistryRemapper<Item> getItemRemapper() {
		return this.itemRemapper;
	}

	@Override
	public RegistryRemapper<Block> getBlockRemapper() {
		return this.blockRemapper;
	}
}
