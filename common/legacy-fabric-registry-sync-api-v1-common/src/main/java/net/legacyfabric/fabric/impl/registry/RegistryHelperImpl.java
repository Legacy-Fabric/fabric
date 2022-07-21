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

package net.legacyfabric.fabric.impl.registry;

import java.util.IdentityHashMap;
import java.util.function.Supplier;

import com.google.common.collect.BiMap;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;

import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.client.registry.sync.ClientRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.ServerRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.ItemCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.RegistriesGetter;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.ArrayAndMapBasedRegistry;

@ApiStatus.Internal
public class RegistryHelperImpl {
	private static final boolean hasFlatteningBegun = VersionUtils.matches(">=1.8 <=1.12.2");
	public static final boolean bootstrap = VersionUtils.matches(">1.8.9");
	public static RegistriesGetter registriesGetter = null;

	public static Block registerBlock(Block block, Identifier id) {
		block.setTranslationKey(formatTranslationKey(id));
		RegistryRemapper<Block> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.BLOCKS);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, block);

		if (hasFlatteningBegun) {
			for (BlockState blockState : block.getStateManager().getBlockStates()) {
				int i = rawId << 4 | block.getData(blockState);
				Block.BLOCK_STATES.set(blockState, i);
			}
		}

		return block;
	}

	public static Item registerItem(Item item, Identifier id) {
		item.setTranslationKey(formatTranslationKey(id));
		RegistryRemapper<Item> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.ITEMS);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, item);

		if (hasFlatteningBegun) {
			if (item instanceof BlockItem) {
				((ItemCompat) item).getBLOCK_ITEMS().put(((BlockItem) item).getBlock(), item);
			}
		}

		return item;
	}

	public static Class<? extends BlockEntity> registerBlockEntity(Class<? extends BlockEntity> blockEntityClass, Identifier id) {
		RegistryRemapper<Class<? extends BlockEntity>> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.BLOCK_ENTITY_TYPES);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, blockEntityClass);

		return blockEntityClass;
	}

	public static StatusEffect registerStatusEffect(StatusEffect statusEffect, Identifier id) {
		statusEffect.setTranslationKey(formatTranslationKey(id));
		RegistryRemapper<StatusEffect> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.STATUS_EFFECTS);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, statusEffect);

		return statusEffect;
	}

	public static StatusEffect registerStatusEffect(RegistryHelper.EntryCreator<StatusEffect> statusEffectCreator, Identifier id) {
		RegistryRemapper<StatusEffect> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.STATUS_EFFECTS);
		int rawId = nextId(registryRemapper.getRegistry());

		((ArrayAndMapBasedRegistry) registryRemapper.getRegistry()).updateArrayLength(rawId);

		StatusEffect statusEffect = statusEffectCreator.create(rawId);
		statusEffect.setTranslationKey(formatTranslationKey(id));
		registryRemapper.register(rawId, id, statusEffect);

		return statusEffect;
	}

	public static Enchantment registerEnchantment(Enchantment enchantment, Identifier id) {
		enchantment.setName(formatTranslationKey(id));
		RegistryRemapper<Enchantment> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.ENCHANTMENTS);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, enchantment);

		return enchantment;
	}

	public static Enchantment registerEnchantment(RegistryHelper.EntryCreator<Enchantment> enchantmentCreator, Identifier id) {
		RegistryRemapper<Enchantment> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.ENCHANTMENTS);
		int rawId = nextId(registryRemapper.getRegistry());

		((ArrayAndMapBasedRegistry) registryRemapper.getRegistry()).updateArrayLength(rawId);

		Enchantment enchantment = enchantmentCreator.create(rawId);
		enchantment.setName(formatTranslationKey(id));
		registryRemapper.register(rawId, id, enchantment);

		return enchantment;
	}

	public static Biome registerBiome(Biome biome, Identifier id) {
		RegistryRemapper<Biome> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.BIOMES);
		int rawId = nextId(registryRemapper.getRegistry());
		registryRemapper.register(rawId, id, biome);

		return biome;
	}

	public static Biome registerBiome(RegistryHelper.EntryCreator<Biome> biomeCreator, Identifier id) {
		RegistryRemapper<Biome> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.BIOMES);
		int rawId = nextId(registryRemapper.getRegistry());

		((ArrayAndMapBasedRegistry) registryRemapper.getRegistry()).updateArrayLength(rawId);

		Biome biome = biomeCreator.create(rawId);
		registryRemapper.register(rawId, id, biome);

		return biome;
	}

	public static Pair<Biome, Biome> registerBiomeWithMutatedVariant(
			RegistryHelper.EntryCreator<Biome> parentBiomeCreator, Identifier parentId,
			RegistryHelper.EntryCreator<Biome> mutatedBiomeCreator, Identifier mutatedId
	) {
		RegistryRemapper<Biome> registryRemapper = RegistryRemapper.getRegistryRemapper(RegistryIds.BIOMES);
		Pair<Integer, Integer> rawIds = nextIds(registryRemapper.getRegistry(), 128);

		((ArrayAndMapBasedRegistry) registryRemapper.getRegistry()).updateArrayLength(rawIds.second());

		Biome parentBiome = parentBiomeCreator.create(rawIds.first());
		registryRemapper.register(rawIds.first(), parentId, parentBiome);

		Biome mutatedBiome = mutatedBiomeCreator.create(rawIds.second());
		registryRemapper.register(rawIds.second(), mutatedId, mutatedBiome);

		return Pair.of(parentBiome, mutatedBiome);
	}

	public static <V> V getValue(Identifier id, Identifier registryId) {
		RegistryRemapper<V> registryRemapper = RegistryRemapper.getRegistryRemapper(registryId);
		return registryRemapper.getRegistry().getValue(id);
	}

	public static RegistryRemapper<?> registerRegistryRemapper(RegistryRemapper<RegistryRemapper<?>> registryRemapperRegistryRemapper, RegistryRemapper<?> registryRemapper) {
		int rawId = nextId(registryRemapperRegistryRemapper.getRegistry());
		registryRemapperRegistryRemapper.register(rawId, registryRemapper.registryId, registryRemapper);

		return registryRemapper;
	}

	public static void registerRegistryRemapper(Supplier<RegistryRemapper<?>> remapperSupplier) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientRegistryRemapper.getInstance().registrerRegistryRemapper(remapperSupplier.get());
		}

		ServerRegistryRemapper.getInstance().registrerRegistryRemapper(remapperSupplier.get());
	}

	public static <V> RegistryRemapper<V> getRegistryRemapper(Identifier identifier) {
		return (RegistryRemapper<V>) ServerRegistryRemapper.getInstance().getRegistryRemapperRegistryRemapper().getRegistry().getValue(identifier);
	}

	private static String formatTranslationKey(Identifier key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public static int nextId(SimpleRegistryCompat<?, ?> registry) {
		return nextId(registry, 0);
	}

	public static int nextId(SimpleRegistryCompat<?, ?> registry, int minId) {
		int id = minId;

		RegistryRemapper<?> registryRemapper = RegistryRemapper.getRegistryRemapper(registry);

		if (registryRemapper == null) {
			registryRemapper = RegistryRemapper.DEFAULT_CLIENT_INSTANCE;
		}

		while (getIdList(registry).fromInt(id) != null
				|| id < registryRemapper.getMinId()
		) {
			id++;
		}

		return id;
	}

	public static Pair<Integer, Integer> nextIds(SimpleRegistryCompat<?, ?> registry, int interval) {
		int id = 0;

		RegistryRemapper<?> registryRemapper = RegistryRemapper.getRegistryRemapper(registry);

		if (registryRemapper == null) {
			registryRemapper = RegistryRemapper.DEFAULT_CLIENT_INSTANCE;
		}

		while (id < registryRemapper.getMinId()
				|| !(getIdList(registry).fromInt(id) == null && getIdList(registry).fromInt(id + interval) == null)
		) {
			id++;
		}

		return Pair.of(id, id + interval);
	}

	public static int nextId(IdListCompat<?> idList, SimpleRegistryCompat<?, ?> registry) {
		int id = 0;

		RegistryRemapper<?> registryRemapper = RegistryRemapper.getRegistryRemapper(registry);

		if (registryRemapper == null) {
			registryRemapper = RegistryRemapper.DEFAULT_CLIENT_INSTANCE;
		}

		while (idList.fromInt(id) != null
				|| id < registryRemapper.getMinId()
		) {
			id++;
		}

		return id;
	}

	public static <K, V> IdListCompat<V> getIdList(SimpleRegistryCompat<K, V> registry) {
		return registry.getIds();
	}

	public static <K, V> BiMap<V, K> getObjects(SimpleRegistryCompat<K, V> registry) {
		return (BiMap<V, K>) registry.getObjects();
	}

	public static <K, V> IdentityHashMap<V, Integer> getIdMap(IdListCompat<V> idList, SimpleRegistryCompat<K, V> registry) {
		return idList.getIdMap(registry);
	}

	public static <K, V> IdentityHashMap<V, Integer> getIdMap(SimpleRegistryCompat<K, V> registry) {
		return getIdMap(getIdList(registry), registry);
	}
}
