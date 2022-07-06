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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.registries.OldBlockEntityRegistry;
import net.legacyfabric.fabric.impl.registry.registries.OldEnchantmentRegistry;
import net.legacyfabric.fabric.impl.registry.registries.OldStatusEffectRegistry;
import net.legacyfabric.fabric.impl.registry.sync.compat.RegistriesGetter;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.mixin.registry.sync.BlockEntityAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.EnchantmentAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.StatusEffectAccessor;

public class RegistrySyncInitializer implements PreLaunchEntrypoint {
	private static SimpleRegistryCompat<String, Class<? extends BlockEntity>> BLOCK_ENTITY_REGISTRY;
	private static SimpleRegistryCompat<Identifier, StatusEffect> STATUS_EFFECT_REGISTRY;
	private static SimpleRegistryCompat<Identifier, Enchantment> ENCHANTMENT_REGISTRY;

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
			public <K> SimpleRegistryCompat<K, StatusEffect> getStatusEffectRegistry() {
				if (STATUS_EFFECT_REGISTRY == null) {
					BiMap<Identifier, StatusEffect> biMap = HashBiMap.create(StatusEffectAccessor.getSTATUS_EFFECTS_BY_ID());
					StatusEffectAccessor.setSTATUS_EFFECTS_BY_ID(biMap);

					STATUS_EFFECT_REGISTRY = new OldStatusEffectRegistry(StatusEffect.STATUS_EFFECTS, biMap) {
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
					BiMap<Identifier, Enchantment> map = HashBiMap.create(EnchantmentAccessor.getENCHANTMENT_MAP());
					EnchantmentAccessor.setENCHANTMENT_MAP(map);
					ENCHANTMENT_REGISTRY = new OldEnchantmentRegistry(EnchantmentAccessor.getENCHANTMENTS(), map) {
						@Override
						public void updateArray() {
							EnchantmentAccessor.setENCHANTMENTS(this.getArray());

							List<Enchantment> enchantments = new ArrayList<>();

							for (Enchantment enchantment : EnchantmentAccessor.getENCHANTMENTS()) {
								if (enchantment != null) {
									enchantments.add(enchantment);
								}
							}

							EnchantmentAccessor.setALL_ENCHANTMENTS(enchantments.toArray(new Enchantment[0]));
						}
					};
				}

				return (SimpleRegistryCompat<K, Enchantment>) ENCHANTMENT_REGISTRY;
			}

			@Override
			public SimpleRegistryCompat<String, Class<? extends BlockEntity>> getBlockEntityRegistry() {
				if (BLOCK_ENTITY_REGISTRY == null) {
					BLOCK_ENTITY_REGISTRY = new OldBlockEntityRegistry(BlockEntityAccessor.getStringClassMap(), BlockEntityAccessor.getClassStringMap());
				}

				return BLOCK_ENTITY_REGISTRY;
			}
		};
	}
}
