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

import net.legacyfabric.fabric.api.biome.BiomeEvents;
import net.legacyfabric.fabric.api.biome.BiomeRegistry;
import net.legacyfabric.fabric.api.block.entity.v1.BlockEntityEvents;
import net.legacyfabric.fabric.api.effect.StatusEffectEvents;
import net.legacyfabric.fabric.api.effect.StatusEffectRegistry;

import net.legacyfabric.fabric.api.enchantment.EnchantmentEvents;
import net.legacyfabric.fabric.api.enchantment.EnchantmentRegistry;
import net.legacyfabric.fabric.api.entity.EntityEvents;

import net.legacyfabric.fabric.api.entity.EntityRegistry;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.api.ItemRegistry;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.entity.living.mob.monster.CreeperEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.CreativeModeTab;
import net.minecraft.item.Item;
import net.minecraft.resource.Identifier;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MutatedBiome;
import net.minecraft.world.biome.PlainsBiome;

import net.legacyfabric.fabric.api.effect.PotionHelper;
import net.legacyfabric.fabric.api.entity.EntityHelper;
import net.legacyfabric.fabric.api.resource.ItemModelRegistry;

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
		BiomeEvents.REGISTER_BIOMES.register(this::registerBiomes);
	}

	private void registerItems() {
		Item testItem = new Item().setCreativeModeTab(CreativeModeTab.FOOD);
		ItemRegistry.register(
				new Identifier("legacy-fabric-api", "test_item"), testItem
		);
		ItemModelRegistry.registerItemModel(testItem, new Identifier("legacy-fabric-api:test_item"));
	}

	private void registerBlocks() {
		List<Object> blockList = new ArrayList<>();

		BlockEvents.REGISTER_BLOCKS.register(() -> {
			Block concBlock = new Block(Material.STONE).setCreativeModeTab(CreativeModeTab.FOOD);
			Block concBlock2 = new Block(Material.STONE).setCreativeModeTab(CreativeModeTab.FOOD);
			Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[]{concBlock, concBlock2} : new Block[]{concBlock2, concBlock};

			for (Block block : blocks) {
				int color = 1644825;

				if (block == concBlock2) {
					color = 3361970;
				}

				Identifier identifier = new Identifier("legacy-fabric-api", "conc_block_" + color);

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
		Identifier identifier = new Identifier("legacy-fabric-api", "test_block_entity");

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
			Identifier effectIdentifier = new Identifier("legacy-fabric-api", "test_effect");
			EFFECT = new TestStatusEffect(effectIdentifier, false, 1234567).setIcon(3, 1).setDurationMultiplier(0.25);
			StatusEffectRegistry.register(effectIdentifier, EFFECT);
			PotionHelper.registerDurationRecipe(EFFECT, "!0 & !1 & !2 & !3 & 1+6");
			PotionHelper.registerAmplifierRecipe(EFFECT, "5");
		});
	}

	private void registerEntities() {
		Identifier creeperId = new Identifier("legacy-fabric-api:test_entity");
		EntityRegistry.register(creeperId, TestCreeperEntity.class);
		EntityHelper.registerSpawnEgg(creeperId, 12222, 563933);
	}

	private void registerEnchantments() {
		Identifier enchantmentId = new Identifier("legacy-fabric-api:test_enchantment");
		EnchantmentRegistry.register(enchantmentId, new TestEnchantment(enchantmentId));
	}

	private void registerBiomes() {
		Identifier biomeId = new Identifier("legacy-fabric-api:test_biome");
		BiomeRegistry.register(biomeId, new TestBiome()
					.setColor(4446496)
					.setTemperatureAndDownfall(0.3F, 0.7F));
	}

	public static class TestBlockWithEntity extends BlockWithBlockEntity {
		protected TestBlockWithEntity(Material material) {
			super(material);
		}

		@Override
		public @Nullable BlockEntity createBlockEntity(World world, int id) {
			return new TestBlockEntity();
		}

		@Override
		public boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction direction, float posX, float posY, float posZ) {
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
		public TestStatusEffect(Identifier identifier, boolean bl, int j) {
			super(REGISTRY_AUTO_ASSIGN_ID, identifier, bl, j);
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
		protected TestEnchantment(Identifier identifier) {
			super(REGISTRY_AUTO_ASSIGN_ID, identifier, 2, EnchantmentCategory.ARMOR_FEET);
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

	public static class TestBiome extends PlainsBiome {
		protected TestBiome() {
			super(REGISTRY_AUTO_ASSIGN_ID);
		}

		@Override
		public Biome mutate(int id) {
			return new MutatedBiome(id, this);
		}
	}
}
