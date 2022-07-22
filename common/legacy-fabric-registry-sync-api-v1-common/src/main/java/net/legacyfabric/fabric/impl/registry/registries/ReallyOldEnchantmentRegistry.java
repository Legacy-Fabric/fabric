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

package net.legacyfabric.fabric.impl.registry.registries;

import com.google.common.collect.BiMap;

import net.minecraft.enchantment.Enchantment;

import net.legacyfabric.fabric.api.registry.v1.EnchantmentIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.util.ArrayBasedRegistry;

public abstract class ReallyOldEnchantmentRegistry extends ArrayBasedRegistry<Enchantment> {
	public ReallyOldEnchantmentRegistry(Enchantment[] valueArray) {
		super(valueArray);
	}

	@Override
	public BiMap<Integer, Identifier> generateIdToKeyMap() {
		BiMap<Integer, Identifier> map = super.generateIdToKeyMap();

		map.put(0, EnchantmentIds.PROTECTION);
		map.put(1, EnchantmentIds.FIRE_PROTECTION);
		map.put(2, EnchantmentIds.FEATHER_FALLING);
		map.put(3, EnchantmentIds.BLAST_PROTECTION);
		map.put(4, EnchantmentIds.PROJECTILE_PROTECTION);
		map.put(5, EnchantmentIds.RESPIRATION);
		map.put(6, EnchantmentIds.AQUA_AFFINITY);
		map.put(7, EnchantmentIds.THORNS);
		map.put(16, EnchantmentIds.SHARPNESS);
		map.put(17, EnchantmentIds.SMITE);
		map.put(18, EnchantmentIds.BANE_OF_ARTHROPODS);
		map.put(19, EnchantmentIds.KNOCKBACK);
		map.put(20, EnchantmentIds.FIRE_ASPECT);
		map.put(21, EnchantmentIds.LOOTING);
		map.put(32, EnchantmentIds.EFFICIENCY);
		map.put(33, EnchantmentIds.SILK_TOUCH);
		map.put(34, EnchantmentIds.UNBREAKING);
		map.put(35, EnchantmentIds.FORTUNE);
		map.put(48, EnchantmentIds.POWER);
		map.put(49, EnchantmentIds.PUNCH);
		map.put(50, EnchantmentIds.FLAME);
		map.put(51, EnchantmentIds.INFINITY);
		map.put(61, EnchantmentIds.LUCK_OF_THE_SEA);
		map.put(62, EnchantmentIds.LURE);

		return map;
	}
}
