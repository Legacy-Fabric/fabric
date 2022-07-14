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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v1.RegistryEntryAddCallback;
import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.registries.OldBlockEntityRegistry;
import net.legacyfabric.fabric.impl.registry.sync.compat.RegistriesGetter;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.mixin.registry.sync.BiomeAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.BlockEntityAccessor;

public class RegistrySyncEarlyInitializer implements PreLaunchEntrypoint {
	private static SimpleRegistryCompat<String, Class<? extends BlockEntity>> BLOCK_ENTITY_REGISTRY;

	@Override
	public void onPreLaunch() {
		RegistryHelperImpl.registriesGetter = new RegistriesGetter() {
			@Override
			public <K> SimpleRegistryCompat<K, Block> getBlockRegistry() {
				return (SimpleRegistryCompat<K, Block>) Block.REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Item> getItemRegistry() {
				return (SimpleRegistryCompat<K, Item>) Item.REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Class<? extends BlockEntity>> getBlockEntityRegistry() {
				if (BLOCK_ENTITY_REGISTRY == null) {
					BLOCK_ENTITY_REGISTRY = new OldBlockEntityRegistry(BlockEntityAccessor.getStringClassMap(), BlockEntityAccessor.getClassStringMap());
				}

				return (SimpleRegistryCompat<K, Class<? extends BlockEntity>>) BLOCK_ENTITY_REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, StatusEffect> getStatusEffectRegistry() {
				return (SimpleRegistryCompat<K, StatusEffect>) StatusEffect.REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Enchantment> getEnchantmentRegistry() {
				return (SimpleRegistryCompat<K, Enchantment>) Enchantment.REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Biome> getBiomeRegistry() {
				return (SimpleRegistryCompat<K, Biome>) Biome.REGISTRY;
			}
		};

		RegistryHelper.onRegistryInitialized(RegistryIds.BIOMES).register(() -> {
			RegistryEntryAddCallback.<Biome>event(RegistryIds.BIOMES).register((rawId, id, biome) -> {
				if (biome.isMutatedBiome()) {
					Biome.biomeList.set(biome, Biome.getBiomeIndex(Biome.REGISTRY.get(new Identifier(((BiomeAccessor) biome).getParent()))));
				}
			});
		});
	}
}
