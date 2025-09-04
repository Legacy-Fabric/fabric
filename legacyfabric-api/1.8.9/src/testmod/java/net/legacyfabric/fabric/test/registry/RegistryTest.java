/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MutatedBiome;
import net.minecraft.world.biome.PlainsBiome;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.effect.PotionHelper;
import net.legacyfabric.fabric.api.entity.EntityHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.resource.ItemModelRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

public class RegistryTest implements ModInitializer {
	public static StatusEffect EFFECT;

	@Override
	public void onInitialize() {
		this.registerItems();
		this.registerBlocks();
		this.registerBlockEntities();
		this.registerEffectsAndPotions();
		this.registerEntities();
		this.registerEnchantments();
		this.registerBiomes();
	}

	private void registerItems() {
		Item testItem = new Item().setItemGroup(ItemGroup.FOOD);
		RegistryHelper.register(
				Item.REGISTRY,
				new Identifier("legacy-fabric-api", "test_item"), testItem
		);
		ItemModelRegistry.registerItemModel(testItem, new Identifier("legacy-fabric-api:test_item"));
	}

	private void registerBlocks() {
		Block concBlock = new Block(Material.STONE, MaterialColor.BLACK).setItemGroup(ItemGroup.FOOD);
		Block concBlock2 = new Block(Material.GLASS, MaterialColor.BLUE).setItemGroup(ItemGroup.FOOD);
		Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[]{concBlock, concBlock2} : new Block[]{concBlock2, concBlock};

		for (Block block : blocks) {
			Identifier identifier = new Identifier("legacy-fabric-api:conc_block_" + block.getMaterialColor(block.getDefaultState()).color);
			RegistryHelper.register(Block.REGISTRY, identifier, block);
			RegistryHelper.register(Item.REGISTRY, identifier, new BlockItem(block));
		}
	}

	private void registerBlockEntities() {
		Identifier identifier = new Identifier("legacy-fabric-api", "test_block_entity");

		Block blockWithEntity = new TestBlockWithEntity(Material.DIRT).setItemGroup(ItemGroup.FOOD);
		RegistryHelper.register(Block.REGISTRY, identifier, blockWithEntity);
		RegistryHelper.register(Item.REGISTRY, identifier, new BlockItem(blockWithEntity));
		RegistryHelper.register(RegistryIds.BLOCK_ENTITY_TYPES, identifier, TestBlockEntity.class);
	}

	private void registerEffectsAndPotions() {
		Identifier identifier = new Identifier("legacy-fabric-api", "test_effect");

		EFFECT = net.legacyfabric.fabric.api.registry.v2.RegistryHelper.register(RegistryIds.STATUS_EFFECTS, identifier,
				id -> new TestStatusEffect(id, identifier, false, 1234567)
						.method_2440(3, 1)
						.method_2434(0.25)
		);
		PotionHelper.registerLevels(EFFECT, "!0 & !1 & !2 & !3 & 1+6");
		PotionHelper.registerAmplifyingFactor(EFFECT, "5");
	}

	private void registerEntities() {
		Identifier creeperId = new Identifier("legacy-fabric-api", "test_entity");
		RegistryHelper.register(RegistryIds.ENTITY_TYPES, creeperId, TestCreeperEntity.class);
		EntityHelper.registerSpawnEgg(creeperId, 12222, 563933);
	}

	private void registerEnchantments() {
		Identifier enchantmentId = new Identifier("legacy-fabric-api", "test_enchantment");
		RegistryHelper.register(RegistryIds.ENCHANTMENTS, enchantmentId, id -> new TestEnchantment(id, enchantmentId));
	}

	private void registerBiomes() {
		Identifier biomeId = new Identifier("legacy-fabric-api", "test_biome");
		RegistryHelper.register(RegistryIds.BIOMES, biomeId,
				id -> new TestBiome(id)
						.setSeedModifier(4446496)
						.setHeight(new Biome.Height(0.525F, 0.95F))
						.setTemperatureAndDownfall(0.3F, 0.7F));
	}

	public static class TestBlockWithEntity extends BlockWithEntity {
		protected TestBlockWithEntity(Material material) {
			super(material);
		}

		@Override
		public @Nullable BlockEntity createBlockEntity(World world, int id) {
			return new TestBlockEntity();
		}

		@Override
		public boolean onUse(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction direction, float posX, float posY, float posZ) {
			if (!world.isClient) {
				BlockEntity entity = world.getBlockEntity(pos);

				if (entity instanceof TestBlockEntity) {
					player.sendMessage(new LiteralText(entity + " at " + pos.toString()));
				}
			}

			return true;
		}
	}

	public static class TestBlockEntity extends BlockEntity {
	}

	public static class TestStatusEffect extends StatusEffect {
		public TestStatusEffect(int i, Identifier identifier, boolean bl, int j) {
			super(i, new net.minecraft.util.Identifier(identifier.toString()), bl, j);
		}

		@Override
		public void method_6087(LivingEntity livingEntity, int i) {
			if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
				livingEntity.heal(1.0F);
			}
		}

		@Override
		public boolean canApplyUpdateEffect(int duration, int amplifier) {
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
					this.ignite();
				}
			}

			super.tick();
		}
	}

	public static class TestEnchantment extends Enchantment {
		protected TestEnchantment(int id, Identifier identifier) {
			super(id, new net.minecraft.util.Identifier(identifier.toString()), 2, EnchantmentTarget.FEET);
		}

		@Override
		public void onDamage(LivingEntity bearer, Entity entity, int power) {
			bearer.addStatusEffect(new StatusEffectInstance(EFFECT.id, 50, 10));
		}

		@Override
		public void onDamaged(LivingEntity bearer, Entity entity, int power) {
			bearer.addStatusEffect(new StatusEffectInstance(EFFECT.id, 50, 10));
		}
	}

	public static class TestBiome extends PlainsBiome {
		protected TestBiome(int id) {
			super(id);
		}

		@Override
		public Biome getMutatedVariant(int id) {
			return new MutatedBiome(id, this);
		}
	}
}
