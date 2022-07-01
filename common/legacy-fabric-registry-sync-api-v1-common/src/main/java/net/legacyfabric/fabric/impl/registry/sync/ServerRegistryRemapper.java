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

package net.legacyfabric.fabric.impl.registry.sync;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ServerRegistryRemapper implements RegistryRemapperAccess {
	private static final RegistryRemapperAccess INSTANCE = new ServerRegistryRemapper();

	private final RegistryRemapper<Item> itemRemapper = new ItemRegistryRemapper();
	private final RegistryRemapper<Block> blockRemapper = new BlockRegistryRemapper();

	@Override
	public RegistryRemapper<Item> getItemRemapper() {
		return this.itemRemapper;
	}

	@Override
	public RegistryRemapper<Block> getBlockRemapper() {
		return this.blockRemapper;
	}

	public static RegistryRemapperAccess getInstance() {
		return INSTANCE;
	}
}
