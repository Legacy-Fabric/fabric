/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.impl.resource.loader;

import java.util.Set;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import net.minecraft.block.Block;
import net.minecraft.client.resource.model.BlockModelProvider;

public class BlockStateVariantRegistryImpl {
	public static final Reference2ReferenceMap<Block, BlockModelProvider> BLOCK_MODEL_PROVIDERS = new Reference2ReferenceOpenHashMap<>();
	public static final Set<Block> BLOCKS = Sets.newHashSet();

	public static void register(Block block, BlockModelProvider provider) {
		BLOCK_MODEL_PROVIDERS.put(block, provider);
	}

	public static void register(Block... blocks) {
		BLOCKS.addAll(Sets.newHashSet(blocks));
	}
}
