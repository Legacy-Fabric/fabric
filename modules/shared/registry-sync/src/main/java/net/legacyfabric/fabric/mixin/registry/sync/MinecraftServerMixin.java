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

package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements RegistryRemapperAccess {
	@Unique
	private final RegistryRemapper itemRemapper = new RegistryRemapper(Item.REGISTRY);
	@Unique
	private final RegistryRemapper blockRemapper = new RegistryRemapper(Block.REGISTRY);

	@Override
	public RegistryRemapper getItemRemapper() {
		return this.itemRemapper;
	}

	@Override
	public RegistryRemapper getBlockRemapper() {
		return this.blockRemapper;
	}
}
