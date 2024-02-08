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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v1.RegistryEntryRemapCallback;
import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.registries.OldBlockEntityRegistry;
import net.legacyfabric.fabric.impl.registry.registries.OldEntityTypeRegistry;
import net.legacyfabric.fabric.impl.registry.registries.ReallyOldBiomeRegistry;
import net.legacyfabric.fabric.impl.registry.registries.ReallyOldEnchantmentRegistry;
import net.legacyfabric.fabric.impl.registry.registries.ReallyOldStatusEffectRegistry;
import net.legacyfabric.fabric.impl.registry.sync.compat.RegistriesGetter;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.OldRemappedRegistry;
import net.legacyfabric.fabric.mixin.registry.sync.BiomeAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.BlockEntityAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.EnchantmentAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.EntityTypeAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.StatusEffectAccessor;

public class RegistrySyncEarlyInitializer implements PreLaunchEntrypoint {
	private static SimpleRegistryCompat<String, Class<? extends BlockEntity>> BLOCK_ENTITY_REGISTRY;
	private static SimpleRegistryCompat<Identifier, StatusEffect> STATUS_EFFECT_REGISTRY;
	private static SimpleRegistryCompat<Identifier, Enchantment> ENCHANTMENT_REGISTRY;
	private static SimpleRegistryCompat<Identifier, Biome> BIOME_REGISTRY;
	private static OldRemappedRegistry<String, Class<? extends Entity>> ENTITY_TYPE_REGISTRY;

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
			public <K> SimpleRegistryCompat<K, Class<? extends BlockEntity>> getBlockEntityTypeRegistry() {
				if (BLOCK_ENTITY_REGISTRY == null) {
					BLOCK_ENTITY_REGISTRY = new OldBlockEntityRegistry(BlockEntityAccessor.getStringClassMap(), BlockEntityAccessor.getClassStringMap());
				}

				return (SimpleRegistryCompat<K, Class<? extends BlockEntity>>) BLOCK_ENTITY_REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, StatusEffect> getStatusEffectRegistry() {
				if (STATUS_EFFECT_REGISTRY == null) {
					STATUS_EFFECT_REGISTRY = new ReallyOldStatusEffectRegistry(StatusEffect.STATUS_EFFECTS) {
						@Override
						public void updateArray() {
							StatusEffectAccessor.setSTATUS_EFFECTS(this.getArray());
						}
					};
				}

				return (SimpleRegistryCompat<K, StatusEffect>) STATUS_EFFECT_REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Enchantment> getEnchantmentRegistry() {
				if (ENCHANTMENT_REGISTRY == null) {
					ENCHANTMENT_REGISTRY = new ReallyOldEnchantmentRegistry(Enchantment.ALL_ENCHANTMENTS) {
						@Override
						public void updateArray() {
							EnchantmentAccessor.setALL_ENCHANTMENTS(this.getArray());

							List<Enchantment> enchantments = new ArrayList<>();

							for (Enchantment enchantment : Enchantment.ALL_ENCHANTMENTS) {
								if (enchantment != null) {
									enchantments.add(enchantment);
								}
							}

							EnchantmentAccessor.setField_5457(enchantments.toArray(new Enchantment[0]));
						}
					};
				}

				return (SimpleRegistryCompat<K, Enchantment>) ENCHANTMENT_REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Biome> getBiomeRegistry() {
				if (BIOME_REGISTRY == null) {
					BIOME_REGISTRY = new ReallyOldBiomeRegistry(BiomeAccessor.getBIOMES()) {
						@Override
						public void updateArray() {
							BiomeAccessor.setBIOMES(this.getArray());
						}
					};
				}

				return (SimpleRegistryCompat<K, Biome>) BIOME_REGISTRY;
			}

			@Override
			public <K> SimpleRegistryCompat<K, Class<? extends Entity>> getEntityTypeRegistry() {
				if (ENTITY_TYPE_REGISTRY == null) {
					BiMap<String, Class<? extends Entity>> NAME_CLASS_MAP = HashBiMap.create(EntityTypeAccessor.getNAME_CLASS_MAP());
					EntityTypeAccessor.setNAME_CLASS_MAP(NAME_CLASS_MAP);
					EntityTypeAccessor.setCLASS_NAME_MAP(NAME_CLASS_MAP.inverse());

					BiMap<Integer, Class<? extends Entity>> ID_CLASS_MAP = HashBiMap.create(EntityTypeAccessor.getID_CLASS_MAP());
					EntityTypeAccessor.setID_CLASS_MAP(ID_CLASS_MAP);
					EntityTypeAccessor.setCLASS_ID_MAP(ID_CLASS_MAP.inverse());

					BiMap<String, Integer> NAME_ID_MAP = HashBiMap.create(EntityTypeAccessor.getNAME_ID_MAP());
					EntityTypeAccessor.setNAME_ID_MAP(NAME_ID_MAP);

					ENTITY_TYPE_REGISTRY = new OldEntityTypeRegistry(NAME_CLASS_MAP, ID_CLASS_MAP, NAME_ID_MAP);
				}

				return (SimpleRegistryCompat<K, Class<? extends Entity>>) ENTITY_TYPE_REGISTRY;
			}
		};

		RegistryHelper.onRegistryInitialized(RegistryIds.BIOMES).register(() -> {
			RegistryEntryRemapCallback.<Biome>event(RegistryIds.BIOMES).register((oldId, newId, key, biome) -> {
				if (biome.id != newId) {
					((BiomeAccessor) biome).setId(newId);
				}
			});
		});

		RegistryHelper.onRegistryInitialized(RegistryIds.ENCHANTMENTS).register(() -> {
			RegistryEntryRemapCallback.<Enchantment>event(RegistryIds.ENCHANTMENTS).register((oldId, newId, key, enchantment) -> {
				if (enchantment.id != newId) {
					((EnchantmentAccessor) enchantment).setId(newId);
				}
			});
		});

		RegistryHelper.onRegistryInitialized(RegistryIds.STATUS_EFFECTS).register(() -> {
			RegistryEntryRemapCallback.<StatusEffect>event(RegistryIds.STATUS_EFFECTS).register((oldId, newId, key, statusEffect) -> {
				if (statusEffect.id != newId) {
					((StatusEffectAccessor) statusEffect).setId(newId);
				}
			});
		});

		//		RegistryHelper.onRegistryInitialized(RegistryIds.ENTITY_TYPES).register(() -> {
		//			for (Map.Entry<String, Integer> entry : EntityTypeAccessor.getNAME_ID_MAP().entrySet()) {
		//				if (EntityType.SPAWN_EGGS.containsKey(ENTITY_TYPE_REGISTRY.getOldKey(entry.getKey()))) {
		//					EntityType.SPAWN_EGGS.put(entry.getKey(), EntityType.SPAWN_EGGS.remove(ENTITY_TYPE_REGISTRY.getOldKey(entry.getKey())));
		//				}
		//			}
		//		});
	}
}
