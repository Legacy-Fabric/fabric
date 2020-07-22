/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.impl.content.registries;

import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.Material;

import net.fabricmc.fabric.api.content.registry.v1.FabricToolItem;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;

public class FabricShovelItem extends FabricToolItem {
	public static final Map<Block, Integer> BLOCKS_BY_MINING_LEVEL = Maps.newHashMap();
	public static final BiConsumer<Block, Integer> MAP_ADDER = BLOCKS_BY_MINING_LEVEL::putIfAbsent;
	public static final BiConsumer<Block, Integer> MAP_REPLACER = BLOCKS_BY_MINING_LEVEL::replace;

	public FabricShovelItem(float attackDamage, ToolMaterial material, Material effectiveMaterial) {
		super(attackDamage, material, effectiveMaterial);
	}

	@Override
	public boolean isEffectiveOn(Block block) {
		if (BLOCKS_BY_MINING_LEVEL.containsKey(block)) {
			return this.material.getMiningLevel() >= BLOCKS_BY_MINING_LEVEL.get(block);
		}

		return false;
	}
}
