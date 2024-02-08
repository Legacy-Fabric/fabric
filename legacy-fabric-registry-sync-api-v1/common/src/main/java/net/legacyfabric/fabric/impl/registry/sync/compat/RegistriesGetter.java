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

package net.legacyfabric.fabric.impl.registry.sync.compat;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

public interface RegistriesGetter {
	<K> SimpleRegistryCompat<K, Block> getBlockRegistry();
	<K> SimpleRegistryCompat<K, Item> getItemRegistry();
	<K> SimpleRegistryCompat<K, Class<? extends BlockEntity>> getBlockEntityTypeRegistry();
	<K> SimpleRegistryCompat<K, StatusEffect> getStatusEffectRegistry();
	<K> SimpleRegistryCompat<K, Enchantment> getEnchantmentRegistry();
	<K> SimpleRegistryCompat<K, Biome> getBiomeRegistry();
	<K> SimpleRegistryCompat<K, Class<? extends Entity>> getEntityTypeRegistry();
}
