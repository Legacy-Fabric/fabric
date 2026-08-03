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

package net.legacyfabric.fabric.test.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import net.legacyfabric.fabric.api.block.entity.v1.BlockEntityEvents;
import net.legacyfabric.fabric.api.effect.StatusEffectEvents;
import net.legacyfabric.fabric.api.effect.StatusEffectRegistry;
import net.legacyfabric.fabric.api.enchantment.EnchantmentEvents;
import net.legacyfabric.fabric.api.enchantment.EnchantmentRegistry;
import net.legacyfabric.fabric.api.entity.EntityEvents;
import net.legacyfabric.fabric.api.entity.EntityRegistry;
import net.legacyfabric.fabric.api.resource.ItemModelRegistry;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.api.ItemRegistry;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.entity.living.mob.monster.CreeperEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.CreativeModeTab;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import net.minecraft.world.biome.PlainsBiome;

import net.legacyfabric.fabric.api.effect.PotionHelper;
import net.legacyfabric.fabric.api.entity.EntityHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

public class RegistryTest implements ModInitializer {
	public static StatusEffect EFFECT;

	@Override
	public void init() {
		ItemEvents.REGISTER_ITEMS.register(this::registerItems);
		this.registerBlocks();
		this.registerBlockEntities();
		this.registerEffectsAndPotions();
		EntityEvents.REGISTER_ENTITIES.register(this::registerEntities);
		EnchantmentEvents.REGISTER_ENCHANTMENTS.register(this::registerEnchantments);
//		this.registerBiomes();
	}

	private void registerItems() {
		Item testItem = new Item()
				.setCreativeModeTab(CreativeModeTab.FOOD)
				.setSpriteName(NamespacedIdentifiers.from("legacy-fabric-api", "test_item").toString());
		ItemRegistry.register(
				NamespacedIdentifiers.from("legacy-fabric-api", "test_item"), testItem
		);
	}

	private void registerBlocks() {
		List<Object> blockList = new ArrayList<>();

		BlockEvents.REGISTER_BLOCKS.register(() -> {
			Block concBlock = new Block(Material.STONE).setCreativeModeTab(CreativeModeTab.FOOD);
			Block concBlock2 = new Block(Material.STONE).setCreativeModeTab(CreativeModeTab.FOOD);
			Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[]{concBlock, concBlock2} : new Block[]{concBlock2, concBlock};

			for (Block block : blocks) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("legacy-fabric-api", "conc_block_" + block.getMaterial().getColor().color);

				BlockRegistry.register(identifier, block);
				blockList.add(block);
			}
		});

		ItemEvents.REGISTER_ITEMS.register(() -> {
			for (Object o : blockList) {
				ItemRegistry.register((Block) o);
			}
		});
	}

	private void registerBlockEntities() {
		NamespacedIdentifier identifier = NamespacedIdentifiers.from("legacy-fabric-api", "test_block_entity");

		AtomicReference<Block> blockWithEntity = new AtomicReference<>();
		BlockEvents.REGISTER_BLOCKS.register(() -> {
			blockWithEntity.set(new TestBlockWithEntity(Material.DIRT).setCreativeModeTab(CreativeModeTab.FOOD));
			BlockRegistry.register(identifier, blockWithEntity.get());
		});

		ItemEvents.REGISTER_ITEMS.register(() -> ItemRegistry.register(blockWithEntity.get()));

		BlockEntityEvents.REGISTER_BLOCK_ENTITIES.register((func) -> {
			func.accept(identifier, TestBlockEntity.class);
		});
	}

	private void registerEffectsAndPotions() {
		StatusEffectEvents.REGISTER_EFFECTS.register(() -> {
			NamespacedIdentifier effectIdentifier = NamespacedIdentifiers.from("legacy-fabric-api", "test_effect");
			EFFECT = new TestStatusEffect(false, 1234567).setIcon(3, 1).setDurationMultiplier(0.25);
			StatusEffectRegistry.register(effectIdentifier, EFFECT);
			PotionHelper.registerDurationRecipe(EFFECT, "!0 & !1 & !2 & !3 & 1+6");
			PotionHelper.registerAmplifierRecipe(EFFECT, "5");
		});
	}

	private void registerEntities() {
		NamespacedIdentifier creeperId = NamespacedIdentifiers.from("legacy-fabric-api", "test_entity");
		EntityRegistry.register(creeperId, TestCreeperEntity.class);
		EntityHelper.registerSpawnEgg(creeperId, 12222, 563933);
	}

	private void registerEnchantments() {
		NamespacedIdentifier enchantmentId = NamespacedIdentifiers.from("legacy-fabric-api", "test_enchantment");
		EnchantmentRegistry.register(enchantmentId, new TestEnchantment());
	}

//	private void registerBiomes() {
//		NamespacedIdentifier biomeId = NamespacedIdentifiers.from("legacy-fabric-api", "test_biome");
//		RegistryHelper.register(RegistryIds.BIOMES, biomeId,
//				id -> new TestBiome(id)
//						.setColor(4446496)
//						.setTemperatureAndDownfall(0.3F, 0.7F));
//	}

	public static class TestBlockWithEntity extends BlockWithBlockEntity {
		protected TestBlockWithEntity(Material material) {
			super(material);
		}

		@Override
		public @Nullable BlockEntity createBlockEntity(World world, int id) {
			return new TestBlockEntity();
		}

		@Override
		public boolean use(World world, int x, int y, int z, PlayerEntity player, int i, float f, float g, float h) {
			if (!world.isMultiplayer) {
				BlockEntity entity = world.getBlockEntity(x, y, z);

				if (entity instanceof TestBlockEntity) {
					player.sendMessage(new LiteralText(entity + " at " + x + "," + y + "," + z));
				}
			}

			return true;
		}
	}

	public static class TestBlockEntity extends BlockEntity {
	}

	public static class TestStatusEffect extends StatusEffect {
		public TestStatusEffect(boolean bl, int j) {
			super(REGISTRY_AUTO_ASSIGN_ID, bl, j);
		}

		@Override
		public void apply(LivingEntity livingEntity, int i) {
			if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
				livingEntity.heal(1.0F);
			}
		}

		@Override
		public boolean shouldApply(int duration, int amplifier) {
			int i;

			i = 50 >> amplifier;

			if (i > 0) {
				return duration % i == 0;
			} else {
				return true;
			}
		}
	}

	public static class TestCreeperEntity extends CreeperEntity {
		public TestCreeperEntity(World world) {
			super(world);
		}

		@Override
		public void tick() {
			if (this.isAlive()) {
				if (this.hasStatusEffect(EFFECT)) {
					this.setIgnited();
				}
			}

			super.tick();
		}
	}

	public static class TestEnchantment extends Enchantment {
		protected TestEnchantment() {
			super(REGISTRY_AUTO_ASSIGN_ID, 2, EnchantmentCategory.ARMOR_FEET);
		}

		@Override
		public void applyDamageWildcard(LivingEntity bearer, Entity entity, int power) {
			bearer.addStatusEffect(new StatusEffectInstance(EFFECT.id, 50, 10));
		}

		@Override
		public void applyProtectionWildcard(LivingEntity bearer, Entity entity, int power) {
			bearer.addStatusEffect(new StatusEffectInstance(EFFECT.id, 50, 10));
		}
	}

//	public static class TestBiome extends PlainsBiome {
//		protected TestBiome(int id) {
//			super(id);
//		}
//	}
}
