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

package net.legacyfabric.fabric.api.registry.v1;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.util.BeforeMC;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;

/**
 * Allows registration of Blocks, Items, Block Entity Classes, Status Effects and Enchantments.
 */
public final class RegistryHelper {
	public static final Map<Identifier, Event<RegistryInitialized>> IDENTIFIER_EVENT_MAP = new HashMap<>();

	public static Event<RegistryInitialized> onRegistryInitialized(Identifier identifier) {
		Event<RegistryInitialized> event;

		if (IDENTIFIER_EVENT_MAP.containsKey(identifier)) {
			event = IDENTIFIER_EVENT_MAP.get(identifier);
		} else {
			event = EventFactory.createArrayBacked(RegistryInitialized.class,
				(callbacks) -> () -> {
					for (RegistryInitialized callback : callbacks) {
						callback.initialized();
					}
				}
			);
			IDENTIFIER_EVENT_MAP.put(identifier, event);
		}

		return event;
	}

	/**
	 * Registers a block with the given ID.
	 *
	 * <p>The Block's translation key is automatically set.</p>
	 *
	 * @param block The block to register
	 * @param id    The ID of the block
	 * @return The block registered
	 */
	public static Block registerBlock(Block block, Identifier id) {
		return RegistryHelperImpl.registerBlock(block, id);
	}

	public static Block getBlock(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryIds.BLOCKS);
	}

	/**
	 * Registers an item with the given ID.
	 *
	 * <p>The Item's translation key is automatically set.</p>
	 *
	 * @param item The item to register
	 * @param id   The ID of the item
	 * @return The item registered
	 */
	public static Item registerItem(Item item, Identifier id) {
		return RegistryHelperImpl.registerItem(item, id);
	}

	public static Item getItem(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryIds.ITEMS);
	}

	/**
	 * Registers a block entity with the given ID.
	 *
	 * @param blockEntityClass The block entity class to register
	 * @param id    The ID of the block entity
	 * @return The block entity class registered
	 */
	public static Class<? extends BlockEntity> registerBlockEntityClass(Class<? extends BlockEntity> blockEntityClass, Identifier id) {
		return RegistryHelperImpl.registerBlockEntity(blockEntityClass, id);
	}

	public static Class<? extends BlockEntity> getBlockEntityClass(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryIds.BLOCK_ENTITIES);
	}

	/**
	 * Registers a status effect with the given ID.
	 *
	 * <p>The Status Effect's translation key is automatically set.</p>
	 *
	 * @param statusEffect The status effect to register
	 * @param id   The ID of the status effect
	 * @return The status effect registered
	 */
	@SinceMC("1.9")
	public static StatusEffect registerStatusEffect(StatusEffect statusEffect, Identifier id) {
		return RegistryHelperImpl.registerStatusEffect(statusEffect, id);
	}

	/**
	 * Registers a status effect with the given ID.
	 *
	 * <p>The Status Effect's translation key is automatically set.</p>
	 *
	 * @param statusEffect The status effect to register
	 * @param id   The ID of the status effect
	 * @return The status effect registered
	 */
	@BeforeMC("1.9")
	public static StatusEffect registerStatusEffect(EntryCreator<StatusEffect> statusEffect, Identifier id) {
		return RegistryHelperImpl.registerStatusEffect(statusEffect, id);
	}

	public static StatusEffect getStatusEffect(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryIds.STATUS_EFFECTS);
	}

	/**
	 * Registers an enchantment with the given ID.
	 *
	 * <p>The Enchantment's translation key is automatically set.</p>
	 *
	 * @param enchantment The enchantment to register
	 * @param id   The ID of the enchantment
	 * @return The enchantment registered
	 */
	@SinceMC("1.9")
	public static Enchantment registerEnchantment(Enchantment enchantment, Identifier id) {
		return RegistryHelperImpl.registerEnchantment(enchantment, id);
	}

	/**
	 * Registers an enchantment with the given ID.
	 *
	 * <p>The Enchantment's translation key is automatically set.</p>
	 *
	 * @param enchantment The enchantment to register
	 * @param id   The ID of the enchantment
	 * @return The enchantment registered
	 */
	@BeforeMC("1.9")
	public static Enchantment registerEnchantment(EntryCreator<Enchantment> enchantment, Identifier id) {
		return RegistryHelperImpl.registerEnchantment(enchantment, id);
	}

	public static Enchantment getEnchantment(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryIds.ENCHANTMENTS);
	}

	/**
	 * Registers a biome with the given ID.
	 *
	 * @param biome The biome to register
	 * @param id   The ID of the biome
	 * @return The biome registered
	 */
	@SinceMC("1.9")
	public static Biome registerBiome(Biome biome, Identifier id) {
		return RegistryHelperImpl.registerBiome(biome, id);
	}

	/**
	 * Registers a biome with the given ID.
	 *
	 * @param biome The biome to register
	 * @param id   The ID of the biome
	 * @return The biome registered
	 */
	@BeforeMC("1.9")
	public static Biome registerBiome(EntryCreator<Biome> biome, Identifier id) {
		return RegistryHelperImpl.registerBiome(biome, id);
	}

	/**
	 * Registers a biome with the given ID and its mutated variant with the other given ID.
	 *
	 * @param parentBiome The biome to register
	 * @param parentId   The ID of the biome
	 * @param mutatedBiome The mutated biome to register
	 * @param mutatedId   The ID of the mutated biome
	 * @return The biomes registered
	 */
	@BeforeMC("1.9")
	public static Pair<Biome, Biome> registerBiomeWithMutatedVariant(
			EntryCreator<Biome> parentBiome, Identifier parentId,
			EntryCreator<Biome> mutatedBiome, Identifier mutatedId
	) {
		return RegistryHelperImpl.registerBiomeWithMutatedVariant(parentBiome, parentId, mutatedBiome, mutatedId);
	}

	public static Biome getBiome(Identifier identifier) {
		return RegistryHelperImpl.getValue(identifier, RegistryIds.BIOMES);
	}

	public interface EntryCreator<T> {
		T create(int rawId);
	}

	public interface RegistryInitialized {
		void initialized();
	}
}
