/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import net.legacyfabric.fabric.api.util.BeforeMC;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;

@Deprecated
public class ItemIds {
	// Block Items
	@SinceMC("1.11")
	public static final Identifier AIR = BlockIds.AIR;
	public static final Identifier STONE = BlockIds.STONE;
	public static final Identifier GRASS_BLOCK = BlockIds.GRASS_BLOCK;
	public static final Identifier DIRT = BlockIds.DIRT;
	public static final Identifier COBBLESTONE = BlockIds.COBBLESTONE;
	public static final Identifier PLANKS = BlockIds.PLANKS;
	public static final Identifier SAPLING = BlockIds.SAPLING;
	public static final Identifier BEDROCK = BlockIds.BEDROCK;
	@BeforeMC("1.8")
	public static final Identifier FLOWING_WATER = BlockIds.FLOWING_WATER;
	@BeforeMC("1.8")
	public static final Identifier WATER = BlockIds.WATER;
	@BeforeMC("1.8")
	public static final Identifier FLOWING_LAVA = BlockIds.FLOWING_LAVA;
	@BeforeMC("1.8")
	public static final Identifier LAVA = BlockIds.LAVA;
	public static final Identifier SAND = BlockIds.SAND;
	public static final Identifier GRAVEL = BlockIds.GRAVEL;
	public static final Identifier GOLD_ORE = BlockIds.GOLD_ORE;
	public static final Identifier IRON_ORE = BlockIds.IRON_ORE;
	public static final Identifier COAL_ORE = BlockIds.COAL_ORE;
	public static final Identifier LOG = BlockIds.LOG;
	public static final Identifier LOG2 = BlockIds.LOG2;
	public static final Identifier LEAVES = BlockIds.LEAVES;
	public static final Identifier LEAVES2 = BlockIds.LEAVES2;
	public static final Identifier SPONGE = BlockIds.SPONGE;
	public static final Identifier GLASS = BlockIds.GLASS;
	public static final Identifier LAPIS_LAZULI_ORE = BlockIds.LAPIS_LAZULI_ORE;
	public static final Identifier LAPIS_LAZULI_BLOCK = BlockIds.LAPIS_LAZULI_BLOCK;
	public static final Identifier DISPENSER = BlockIds.DISPENSER;
	public static final Identifier SANDSTONE = BlockIds.SANDSTONE;
	public static final Identifier NOTEBLOCK = BlockIds.NOTEBLOCK;
	public static final Identifier POWERED_RAIL = BlockIds.POWERED_RAIL;
	public static final Identifier DETECTOR_RAIL = BlockIds.DETECTOR_RAIL;
	public static final Identifier STICKY_PISTON = BlockIds.STICKY_PISTON;
	public static final Identifier COBWEB = BlockIds.COBWEB;
	public static final Identifier TALL_GRASS = BlockIds.TALL_GRASS;
	public static final Identifier DEAD_BUSH = BlockIds.DEAD_BUSH;
	public static final Identifier PISTON = BlockIds.PISTON;
	public static final Identifier WOOL = BlockIds.WOOL;
	public static final Identifier YELLOW_FLOWER = BlockIds.YELLOW_FLOWER;
	public static final Identifier RED_FLOWER = BlockIds.RED_FLOWER;
	public static final Identifier BROWN_MUSHROOM = BlockIds.BROWN_MUSHROOM;
	public static final Identifier RED_MUSHROOM = BlockIds.RED_MUSHROOM;
	public static final Identifier GOLD_BLOCK = BlockIds.GOLD_BLOCK;
	public static final Identifier IRON_BLOCK = BlockIds.IRON_BLOCK;
	@BeforeMC("1.8")
	public static final Identifier DOUBLE_STONE_SLAB = BlockIds.DOUBLE_STONE_SLAB;
	public static final Identifier STONE_SLAB = BlockIds.STONE_SLAB;
	public static final Identifier BRICKS = BlockIds.BRICKS;
	public static final Identifier TNT = BlockIds.TNT;
	public static final Identifier BOOKSHELF = BlockIds.BOOKSHELF;
	public static final Identifier MOSSY_COBBLESTONE = BlockIds.MOSSY_COBBLESTONE;
	public static final Identifier OBSIDIAN = BlockIds.OBSIDIAN;
	public static final Identifier TORCH = BlockIds.TORCH;
	@BeforeMC("1.8")
	public static final Identifier FIRE = BlockIds.FIRE;
	@SinceMC("1.9")
	public static final Identifier END_ROD = BlockIds.END_ROD;
	@SinceMC("1.9")
	public static final Identifier CHORUS_PLANT = BlockIds.CHORUS_PLANT;
	@SinceMC("1.9")
	public static final Identifier CHORUS_FLOWER = BlockIds.CHORUS_FLOWER;
	@SinceMC("1.9")
	public static final Identifier PURPUR_BLOCK = BlockIds.PURPUR_BLOCK;
	@SinceMC("1.9")
	public static final Identifier PURPUR_PILLAR = BlockIds.PURPUR_PILLAR;
	@SinceMC("1.9")
	public static final Identifier PURPUR_STAIRS = BlockIds.PURPUR_STAIRS;
	@SinceMC("1.9")
	public static final Identifier PURPUR_SLAB = BlockIds.PURPUR_SLAB;
	public static final Identifier SPAWNER = BlockIds.SPAWNER;
	public static final Identifier OAK_STAIRS = BlockIds.OAK_STAIRS;
	public static final Identifier CHEST = BlockIds.CHEST;
	public static final Identifier DIAMOND_ORE = BlockIds.DIAMOND_ORE;
	public static final Identifier DIAMOND_BLOCK = BlockIds.DIAMOND_BLOCK;
	public static final Identifier CRAFTING_TABLE = BlockIds.CRAFTING_TABLE;
	public static final Identifier FARMLAND = BlockIds.FARMLAND;
	public static final Identifier FURNACE = BlockIds.FURNACE;
	public static final Identifier LIT_FURNACE = BlockIds.LIT_FURNACE;
	public static final Identifier LADDER = BlockIds.LADDER;
	public static final Identifier RAIL = BlockIds.RAIL;
	public static final Identifier STONE_STAIRS = BlockIds.STONE_STAIRS;
	public static final Identifier LEVER = BlockIds.LEVER;
	public static final Identifier STONE_PRESSURE_PLATE = BlockIds.STONE_PRESSURE_PLATE;
	public static final Identifier WOODEN_PRESSURE_PLATE = BlockIds.WOODEN_PRESSURE_PLATE;
	public static final Identifier REDSTONE_ORE = BlockIds.REDSTONE_ORE;
	public static final Identifier REDSTONE_TORCH = BlockIds.REDSTONE_TORCH;
	public static final Identifier STONE_BUTTON = BlockIds.STONE_BUTTON;
	public static final Identifier SNOW_LAYER = BlockIds.SNOW_LAYER;
	public static final Identifier ICE = BlockIds.ICE;
	public static final Identifier SNOW_BLOCK = BlockIds.SNOW_BLOCK;
	public static final Identifier CACTUS = BlockIds.CACTUS;
	public static final Identifier CLAY = BlockIds.CLAY;
	public static final Identifier JUKEBOX = BlockIds.JUKEBOX;
	public static final Identifier OAK_FENCE = BlockIds.OAK_FENCE;
	@SinceMC("1.8")
	public static final Identifier SPRUCE_FENCE = BlockIds.SPRUCE_FENCE;
	@SinceMC("1.8")
	public static final Identifier BIRCH_FENCE = BlockIds.BIRCH_FENCE;
	@SinceMC("1.8")
	public static final Identifier JUNGLE_FENCE = BlockIds.JUNGLE_FENCE;
	@SinceMC("1.8")
	public static final Identifier DARK_OAK_FENCE = BlockIds.DARK_OAK_FENCE;
	@SinceMC("1.8")
	public static final Identifier ACACIA_FENCE = BlockIds.ACACIA_FENCE;
	public static final Identifier PUMPKIN = BlockIds.PUMPKIN;
	public static final Identifier NETHERRACK = BlockIds.NETHERRACK;
	public static final Identifier SOULSAND = BlockIds.SOULSAND;
	public static final Identifier GLOWSTONE = BlockIds.GLOWSTONE;
	@BeforeMC("1.8")
	public static final Identifier PORTAL = BlockIds.PORTAL;
	public static final Identifier JACK_O_LANTERN = BlockIds.JACK_O_LANTERN;
	public static final Identifier TRAPDOOR = BlockIds.TRAPDOOR;
	public static final Identifier MONSTER_EGG = BlockIds.MONSTER_EGG;
	public static final Identifier STONE_BRICKS = BlockIds.STONE_BRICKS;
	public static final Identifier BROWN_MUSHROOM_BLOCK = BlockIds.BROWN_MUSHROOM_BLOCK;
	public static final Identifier RED_MUSHROOM_BLOCK = BlockIds.RED_MUSHROOM_BLOCK;
	public static final Identifier IRON_BARS = BlockIds.IRON_BARS;
	public static final Identifier GLASS_PANE = BlockIds.GLASS_PANE;
	public static final Identifier MELON_BLOCK = BlockIds.MELON_BLOCK;
	public static final Identifier VINE = BlockIds.VINE;
	public static final Identifier OAK_FENCE_GATE = BlockIds.OAK_FENCE_GATE;
	@SinceMC("1.8")
	public static final Identifier SPRUCE_FENCE_GATE = BlockIds.SPRUCE_FENCE_GATE;
	@SinceMC("1.8")
	public static final Identifier BIRCH_FENCE_GATE = BlockIds.BIRCH_FENCE_GATE;
	@SinceMC("1.8")
	public static final Identifier JUNGLE_FENCE_GATE = BlockIds.JUNGLE_FENCE_GATE;
	@SinceMC("1.8")
	public static final Identifier DARK_OAK_FENCE_GATE = BlockIds.DARK_OAK_FENCE_GATE;
	@SinceMC("1.8")
	public static final Identifier ACACIA_FENCE_GATE = BlockIds.ACACIA_FENCE_GATE;
	public static final Identifier BRICK_STAIRS = BlockIds.BRICK_STAIRS;
	public static final Identifier STONE_BRICK_STAIRS = BlockIds.STONE_BRICK_STAIRS;
	public static final Identifier MYCELIUM = BlockIds.MYCELIUM;
	public static final Identifier LILY_PAD = BlockIds.LILY_PAD;
	public static final Identifier NETHER_BRICKS = BlockIds.NETHER_BRICKS;
	public static final Identifier NETHER_BRICK_FENCE = BlockIds.NETHER_BRICK_FENCE;
	public static final Identifier NETHER_BRICK_STAIRS = BlockIds.NETHER_BRICK_STAIRS;
	public static final Identifier ENCHANTING_TABLE = BlockIds.ENCHANTING_TABLE;
	@BeforeMC("1.8")
	public static final Identifier END_PORTAL = BlockIds.END_PORTAL;
	public static final Identifier END_PORTAL_FRAME = BlockIds.END_PORTAL_FRAME;
	public static final Identifier END_STONE = BlockIds.END_STONE;
	@SinceMC("1.9")
	public static final Identifier END_STONE_BRICKS = BlockIds.END_STONE_BRICKS;
	public static final Identifier DRAGON_EGG = BlockIds.DRAGON_EGG;
	public static final Identifier REDSTONE_LAMP = BlockIds.REDSTONE_LAMP;
	@BeforeMC("1.8")
	public static final Identifier DOUBLE_WOODEN_SLAB = BlockIds.DOUBLE_WOODEN_SLAB;
	public static final Identifier WOODEN_SLAB = BlockIds.WOODEN_SLAB;
	@BeforeMC("1.8")
	public static final Identifier COCOA_BLOCK = BlockIds.COCOA;
	public static final Identifier SANDSTONE_STAIRS = BlockIds.SANDSTONE_STAIRS;
	public static final Identifier EMERALD_ORE = BlockIds.EMERALD_ORE;
	public static final Identifier ENDER_CHEST = BlockIds.ENDER_CHEST;
	public static final Identifier TRIPWIRE_HOOK = BlockIds.TRIPWIRE_HOOK;
	public static final Identifier EMERALD_BLOCK = BlockIds.EMERALD_BLOCK;
	public static final Identifier SPRUCE_STAIRS = BlockIds.SPRUCE_STAIRS;
	public static final Identifier BIRCH_STAIRS = BlockIds.BIRCH_STAIRS;
	public static final Identifier JUNGLE_STAIRS = BlockIds.JUNGLE_STAIRS;
	public static final Identifier COMMAND_BLOCK = BlockIds.COMMAND_BLOCK;
	public static final Identifier BEACON = BlockIds.BEACON;
	public static final Identifier COBBLESTONE_WALL = BlockIds.COBBLESTONE_WALL;
	@BeforeMC("1.8")
	public static final Identifier CARROT_CROPS = BlockIds.CARROT_CROPS;
	@BeforeMC("1.8")
	public static final Identifier POTATO_CROPS = BlockIds.POTATO_CROPS;
	public static final Identifier WOODEN_BUTTON = BlockIds.WOODEN_BUTTON;
	public static final Identifier ANVIL = BlockIds.ANVIL;
	public static final Identifier TRAPPED_CHEST = BlockIds.TRAPPED_CHEST;
	public static final Identifier LIGHT_WEIGHTED_PRESSURE_PLATE = BlockIds.LIGHT_WEIGHTED_PRESSURE_PLATE;
	public static final Identifier HEAVY_WEIGHTED_PRESSURE_PLATE = BlockIds.HEAVY_WEIGHTED_PRESSURE_PLATE;
	public static final Identifier DAYLIGHT_DETECTOR = BlockIds.DAYLIGHT_DETECTOR;
	public static final Identifier REDSTONE_BLOCK = BlockIds.REDSTONE_BLOCK;
	public static final Identifier NETHER_QUARTZ_ORE = BlockIds.NETHER_QUARTZ_ORE;
	public static final Identifier HOPPER = BlockIds.HOPPER;
	public static final Identifier QUARTZ_BLOCK = BlockIds.QUARTZ_BLOCK;
	public static final Identifier QUARTZ_STAIRS = BlockIds.QUARTZ_STAIRS;
	public static final Identifier ACTIVATOR_RAIL = BlockIds.ACTIVATOR_RAIL;
	public static final Identifier DROPPER = BlockIds.DROPPER;
	public static final Identifier STAINED_TERRACOTTA = BlockIds.STAINED_TERRACOTTA;
	@SinceMC("1.8")
	public static final Identifier BARRIER = BlockIds.BARRIER;
	@SinceMC("1.8")
	public static final Identifier IRON_TRAPDOOR = BlockIds.IRON_TRAPDOOR;
	public static final Identifier HAY_BALE = BlockIds.HAY_BALE;
	public static final Identifier CARPET = BlockIds.CARPET;
	public static final Identifier TERRACOTTA = BlockIds.TERRACOTTA;
	public static final Identifier COAL_BLOCK = BlockIds.COAL_BLOCK;
	public static final Identifier PACKED_ICE = BlockIds.PACKED_ICE;
	public static final Identifier ACACIA_STAIRS = BlockIds.ACACIA_STAIRS;
	public static final Identifier DARK_OAK_STAIRS = BlockIds.DARK_OAK_STAIRS;
	@SinceMC("1.8")
	public static final Identifier SLIME_BLOCK = BlockIds.SLIME_BLOCK;
	public static final Identifier DOUBLE_PLANT = BlockIds.DOUBLE_PLANT;
	@SinceMC("1.9")
	public static final Identifier GRASS_PATH = BlockIds.GRASS_PATH;
	public static final Identifier STAINED_GLASS = BlockIds.STAINED_GLASS;
	public static final Identifier STAINED_GLASS_PANE = BlockIds.STAINED_GLASS_PANE;
	@SinceMC("1.8")
	public static final Identifier PRISMARINE = BlockIds.PRISMARINE;
	@SinceMC("1.8")
	public static final Identifier SEA_LANTERN = BlockIds.SEA_LANTERN;
	@SinceMC("1.8")
	public static final Identifier RED_SANDSTONE = BlockIds.RED_SANDSTONE;
	@SinceMC("1.8")
	public static final Identifier RED_SANDSTONE_STAIRS = BlockIds.RED_SANDSTONE_STAIRS;
	@SinceMC("1.8")
	public static final Identifier RED_SANDSTONE_SLAB = BlockIds.RED_SANDSTONE_SLAB;
	@SinceMC("1.9")
	public static final Identifier REPEATING_COMMAND_BLOCK = BlockIds.REPEATING_COMMAND_BLOCK;
	@SinceMC("1.9")
	public static final Identifier CHAIN_COMMAND_BLOCK = BlockIds.CHAIN_COMMAND_BLOCK;
	@SinceMC("1.10")
	public static final Identifier MAGMA_BLOCK = BlockIds.MAGMA_BLOCK;
	@SinceMC("1.10")
	public static final Identifier NETHER_WART_BLOCK = BlockIds.NETHER_WART_BLOCK;
	@SinceMC("1.10")
	public static final Identifier RED_NETHER_BRICKS = BlockIds.RED_NETHER_BRICKS;
	@SinceMC("1.10")
	public static final Identifier BONE_BLOCK = BlockIds.BONE_BLOCK;
	@SinceMC("1.10")
	public static final Identifier STRUCTURE_VOID = BlockIds.STRUCTURE_VOID;
	@SinceMC("1.11")
	public static final Identifier OBSERVER = BlockIds.OBSERVER;
	@SinceMC("1.11")
	public static final Identifier WHITE_SHULKER_BOX = BlockIds.WHITE_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier ORANGE_SHULKER_BOX = BlockIds.ORANGE_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier MAGENTA_SHULKER_BOX = BlockIds.MAGENTA_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier LIGHT_BLUE_SHULKER_BOX = BlockIds.LIGHT_BLUE_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier YELLOW_SHULKER_BOX = BlockIds.YELLOW_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier LIME_SHULKER_BOX = BlockIds.LIME_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier PINK_SHULKER_BOX = BlockIds.PINK_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier GRAY_SHULKER_BOX = BlockIds.GRAY_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier LIGHT_GRAY_SHULKER_BOX = BlockIds.LIGHT_GRAY_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier CYAN_SHULKER_BOX = BlockIds.CYAN_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier PURPLE_SHULKER_BOX = BlockIds.PURPLE_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier BLUE_SHULKER_BOX = BlockIds.BLUE_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier BROWN_SHULKER_BOX = BlockIds.BROWN_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier GREEN_SHULKER_BOX = BlockIds.GREEN_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier RED_SHULKER_BOX = BlockIds.RED_SHULKER_BOX;
	@SinceMC("1.11")
	public static final Identifier BLACK_SHULKER_BOX = BlockIds.BLACK_SHULKER_BOX;
	@SinceMC("1.12")
	public static final Identifier WHITE_GLAZED_TERRACOTTA = BlockIds.WHITE_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier ORANGE_GLAZED_TERRACOTTA = BlockIds.ORANGE_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier MAGENTA_GLAZED_TERRACOTTA = BlockIds.MAGENTA_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier LIGHT_BLUE_GLAZED_TERRACOTTA = BlockIds.LIGHT_BLUE_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier YELLOW_GLAZED_TERRACOTTA = BlockIds.YELLOW_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier LIME_GLAZED_TERRACOTTA = BlockIds.LIME_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier PINK_GLAZED_TERRACOTTA = BlockIds.PINK_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier GRAY_GLAZED_TERRACOTTA = BlockIds.GRAY_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier LIGHT_GRAY_GLAZED_TERRACOTTA = BlockIds.LIGHT_GRAY_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier CYAN_GLAZED_TERRACOTTA = BlockIds.CYAN_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier PURPLE_GLAZED_TERRACOTTA = BlockIds.PURPLE_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier BLUE_GLAZED_TERRACOTTA = BlockIds.BLUE_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier BROWN_GLAZED_TERRACOTTA = BlockIds.BROWN_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier GREEN_GLAZED_TERRACOTTA = BlockIds.GREEN_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier RED_GLAZED_TERRACOTTA = BlockIds.RED_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier BLACK_GLAZED_TERRACOTTA = BlockIds.BLACK_GLAZED_TERRACOTTA;
	@SinceMC("1.12")
	public static final Identifier CONCRETE = BlockIds.CONCRETE;
	@SinceMC("1.12")
	public static final Identifier CONCRETE_POWDER = BlockIds.CONCRETE_POWDER;
	@SinceMC("1.10")
	public static final Identifier STRUCTURE_BLOCK = BlockIds.STRUCTURE_BLOCK;

	// Items
	public static final Identifier IRON_SHOVEL = id("iron_shovel");
	public static final Identifier IRON_PICKAXE = id("iron_pickaxe");
	public static final Identifier IRON_AXE = id("iron_axe");
	public static final Identifier FLINT_AND_STEEL = id("flint_and_steel");
	public static final Identifier APPLE = id("apple");
	public static final Identifier BOW = id("bow");
	public static final Identifier ARROW = id("arrow");
	public static final Identifier COAL = id("coal");
	public static final Identifier DIAMOND = id("diamond");
	public static final Identifier IRON_INGOT = id("iron_ingot");
	public static final Identifier GOLD_INGOT = id("gold_ingot");
	public static final Identifier IRON_SWORD = id("iron_sword");
	public static final Identifier WOODEN_SWORD = id("wooden_sword");
	public static final Identifier WOODEN_SHOVEL = id("wooden_shovel");
	public static final Identifier WOODEN_PICKAXE = id("wooden_pickaxe");
	public static final Identifier WOODEN_AXE = id("wooden_axe");
	public static final Identifier STONE_SWORD = id("stone_sword");
	public static final Identifier STONE_SHOVEL = id("stone_shovel");
	public static final Identifier STONE_PICKAXE = id("stone_pickaxe");
	public static final Identifier STONE_AXE = id("stone_axe");
	public static final Identifier DIAMOND_SWORD = id("diamond_sword");
	public static final Identifier DIAMOND_SHOVEL = id("diamond_shovel");
	public static final Identifier DIAMOND_PICKAXE = id("diamond_pickaxe");
	public static final Identifier DIAMOND_AXE = id("diamond_axe");
	public static final Identifier STICK = id("stick");
	public static final Identifier BOWL = id("bowl");
	public static final Identifier MUSHROOM_STEW = id("mushroom_stew");
	public static final Identifier GOLDEN_SWORD = id("golden_sword");
	public static final Identifier GOLDEN_SHOVEL = id("golden_shovel");
	public static final Identifier GOLDEN_PICKAXE = id("golden_pickaxe");
	public static final Identifier GOLDEN_AXE = id("golden_axe");
	public static final Identifier STRING = id("string");
	public static final Identifier FEATHER = id("feather");
	public static final Identifier GUNPOWDER = id("gunpowder");
	public static final Identifier WOODEN_HOE = id("wooden_hoe");
	public static final Identifier STONE_HOE = id("stone_hoe");
	public static final Identifier IRON_HOE = id("iron_hoe");
	public static final Identifier DIAMOND_HOE = id("diamond_hoe");
	public static final Identifier GOLDEN_HOE = id("golden_hoe");
	public static final Identifier WHEAT_SEEDS = id("wheat_seeds");
	public static final Identifier WHEAT = id("wheat");
	public static final Identifier BREAD = id("bread");
	public static final Identifier LEATHER_HELMET = id("leather_helmet");
	public static final Identifier LEATHER_CHESTPLATE = id("leather_chestplate");
	public static final Identifier LEATHER_LEGGINGS = id("leather_leggings");
	public static final Identifier LEATHER_BOOTS = id("leather_boots");
	public static final Identifier CHAINMAIL_HELMET = id("chainmail_helmet");
	public static final Identifier CHAINMAIL_CHESTPLATE = id("chainmail_chestplate");
	public static final Identifier CHAINMAIL_LEGGINGS = id("chainmail_leggings");
	public static final Identifier CHAINMAIL_BOOTS = id("chainmail_boots");
	public static final Identifier IRON_HELMET = id("iron_helmet");
	public static final Identifier IRON_CHESTPLATE = id("iron_chestplate");
	public static final Identifier IRON_LEGGINGS = id("iron_leggings");
	public static final Identifier IRON_BOOTS = id("iron_boots");
	public static final Identifier DIAMOND_HELMET = id("diamond_helmet");
	public static final Identifier DIAMOND_CHESTPLATE = id("diamond_chestplate");
	public static final Identifier DIAMOND_LEGGINGS = id("diamond_leggings");
	public static final Identifier DIAMOND_BOOTS = id("diamond_boots");
	public static final Identifier GOLDEN_HELMET = id("golden_helmet");
	public static final Identifier GOLDEN_CHESTPLATE = id("golden_chestplate");
	public static final Identifier GOLDEN_LEGGINGS = id("golden_leggings");
	public static final Identifier GOLDEN_BOOTS = id("golden_boots");
	public static final Identifier FLINT = id("flint");
	public static final Identifier RAW_PORKCHOP = id("porkchop");
	public static final Identifier COOKED_PORKCHOP = id("cooked_porkchop");
	public static final Identifier PAINTING = id("painting");
	public static final Identifier GOLDEN_APPLE = id("golden_apple");
	public static final Identifier SIGN = id("sign");
	public static final Identifier OAK_DOOR = id("wooden_door");
	public static final Identifier BUCKET = id("bucket");
	public static final Identifier WATER_BUCKET = id("water_bucket");
	public static final Identifier LAVA_BUCKET = id("lava_bucket");
	public static final Identifier MINECART = id("minecart");
	public static final Identifier SADDLE = id("saddle");
	public static final Identifier IRON_DOOR = id("iron_door");
	public static final Identifier REDSTONE = id("redstone");
	public static final Identifier SNOWBALL = id("snowball");
	public static final Identifier OAK_BOAT = id("boat");
	public static final Identifier LEATHER = id("leather");
	public static final Identifier MILK_BUCKET = id("milk_bucket");
	public static final Identifier BRICK = id("brick");
	public static final Identifier CLAY_BALL = id("clay_ball");
	public static final Identifier SUGARCANE = id("reeds");
	public static final Identifier PAPER = id("paper");
	public static final Identifier BOOK = id("book");
	public static final Identifier SLIME_BALL = id("slime_ball");
	public static final Identifier MINECART_WITH_CHEST = id("chest_minecart");
	public static final Identifier MINECART_WITH_FURNACE = id("furnace_minecart");
	public static final Identifier EGG = id("egg");
	public static final Identifier COMPASS = id("compass");
	public static final Identifier FISHING_ROD = id("fishing_rod");
	public static final Identifier CLOCK = id("clock");
	public static final Identifier GLOWSTONE_DUST = id("glowstone_dust");
	public static final Identifier RAW_FISH = id("fish");
	public static final Identifier COOKED_FISH = id("cooked_fished");
	public static final Identifier DYE = id("dye");
	public static final Identifier BONE = id("bone");
	public static final Identifier SUGAR = id("sugar");
	public static final Identifier CAKE = id("cake");
	public static final Identifier BED = id("bed");
	public static final Identifier REPEATER = id("repeater");
	public static final Identifier COOKIE = id("cookie");
	public static final Identifier FILLED_MAP = id("filled_map");
	public static final Identifier SHEARS = id("shears");
	public static final Identifier MELON = id("melon");
	public static final Identifier PUMPKIN_SEEDS = id("pumpkin_seeds");
	public static final Identifier MELON_SEEDS = id("melon_seeds");
	public static final Identifier RAW_BEEF = id("beef");
	public static final Identifier STEAK = id("cooked_beef");
	public static final Identifier RAW_CHICKEN = id("chicken");
	public static final Identifier COOKED_CHICKEN = id("cooked_chicken");
	public static final Identifier ROTTEN_FLESH = id("rotten_flesh");
	public static final Identifier ENDER_PEARL = id("ender_pearl");
	public static final Identifier BLAZE_ROD = id("blaze_rod");
	public static final Identifier GHAST_TEAR = id("ghast_tear");
	public static final Identifier GOLD_NUGGET = id("gold_nugget");
	public static final Identifier NETHER_WART = id("nether_wart");
	public static final Identifier POTION = id("potion");
	public static final Identifier GLASS_BOTTLE = id("glass_bottle");
	public static final Identifier SPIDER_EYE = id("spider_eye");
	public static final Identifier FERMENTED_SPIDER_EYE = id("fermented_spider_eye");
	public static final Identifier BLAZE_POWDER = id("blaze_powder");
	public static final Identifier MAGMA_CREAM = id("magma_cream");
	public static final Identifier BREWING_STAND = id("brewing_stand");
	public static final Identifier CAULDRON = id("cauldron");
	public static final Identifier EYE_OF_ENDER = id("ender_eye");
	public static final Identifier GLISTERING_MELON = id("speckled_melon");
	public static final Identifier SPAWN_EGG = id("spawn_egg");
	public static final Identifier EXPERIENCE_BOTTLE = id("experience_bottle");
	public static final Identifier FIRE_CHARGE = id("fire_charge");
	public static final Identifier WRITABLE_BOOK = id("writable_book");
	public static final Identifier WRITTEN_BOOK = id("written_book");
	public static final Identifier EMERALD = id("emerald");
	public static final Identifier ITEM_FRAME = id("item_frame");
	public static final Identifier FLOWER_POT = id("flower_pot");
	public static final Identifier CARROT = id("carrot");
	public static final Identifier POTATO = id("potato");
	public static final Identifier BAKED_POTATO = id("baked_potato");
	public static final Identifier POISONOUS_POTATO = id("poisonous_potato");
	public static final Identifier MAP = id("map");
	public static final Identifier GOLDEN_CARROT = id("golden_carrot");
	public static final Identifier SKULL = id("skull");
	public static final Identifier CARROT_ON_A_STICK = id("carrot_on_a_stick");
	public static final Identifier NETHER_STAR = id("nether_star");
	public static final Identifier PUMPKIN_PIE = id("pumpkin_pie");
	public static final Identifier FIREWORKS = id("fireworks");
	public static final Identifier FIREWORK_CHARGE = id("firework_charge");
	public static final Identifier ENCHANTED_BOOK = id("enchanted_book");
	public static final Identifier COMPARATOR = id("comparator");
	public static final Identifier NETHER_BRICK = id("netherbrick");
	public static final Identifier QUARTZ = id("quartz");
	public static final Identifier MINECART_WITH_TNT = id("tnt_minecart");
	public static final Identifier MINECART_WITH_HOPPER = id("hopper_minecart");
	@SinceMC("1.8")
	public static final Identifier PRISMARINE_SHARD = id("prismarine_shard");
	@SinceMC("1.8")
	public static final Identifier PRISMARINE_CRYSTALS = id("prismarine_crystals");
	@SinceMC("1.8")
	public static final Identifier RAW_RABBIT = id("rabbit");
	@SinceMC("1.8")
	public static final Identifier COOKED_RABBIT = id("cooked_rabbit");
	@SinceMC("1.8")
	public static final Identifier RABBIT_STEW = id("rabbit_stew");
	@SinceMC("1.8")
	public static final Identifier RABBIT_FOOT = id("rabbit_foot");
	@SinceMC("1.8")
	public static final Identifier RABBIT_HIDE = id("rabbit_hide");
	@SinceMC("1.8")
	public static final Identifier ARMOR_STAND = id("armor_stand");
	public static final Identifier IRON_HORSE_ARMOR = id("iron_horse_armor");
	public static final Identifier GOLDEN_HORSE_ARMOR = id("golden_horse_armor");
	public static final Identifier DIAMOND_HORSE_ARMOR = id("diamond_horse_armor");
	public static final Identifier LEAD = id("lead");
	public static final Identifier NAME_TAG = id("name_tag");
	public static final Identifier MINECART_WITH_COMMAND_BLOCK = id("command_block_minecart");
	@SinceMC("1.8")
	public static final Identifier RAW_MUTTON = id("mutton");
	@SinceMC("1.8")
	public static final Identifier COOKED_MUTTON = id("cooked_mutton");
	@SinceMC("1.8")
	public static final Identifier BANNER = id("banner");
	@SinceMC("1.9")
	public static final Identifier END_CRYSTAL = id("end_crystal");
	@SinceMC("1.8")
	public static final Identifier SPRUCE_DOOR = id("spruce_door");
	@SinceMC("1.8")
	public static final Identifier BIRCH_DOOR = id("birch_door");
	@SinceMC("1.8")
	public static final Identifier JUNGLE_DOOR = id("jungle_door");
	@SinceMC("1.8")
	public static final Identifier ACACIA_DOOR = id("acacia_door");
	@SinceMC("1.8")
	public static final Identifier DARK_OAK_DOOR = id("dark_oak_door");
	@SinceMC("1.9")
	public static final Identifier CHORUS_FRUIT = id("chorus_fruit");
	@SinceMC("1.9")
	public static final Identifier CHORUS_FRUIT_POPPED = id("chorus_fruit_popped");
	@SinceMC("1.9")
	public static final Identifier BEETROOT = id("beetroot");
	@SinceMC("1.9")
	public static final Identifier BEETROOT_SEEDS = id("beetroot_seeds");
	@SinceMC("1.9")
	public static final Identifier BEETROOT_SOUP = id("beetroot_soup");
	@SinceMC("1.9")
	public static final Identifier DRAGON_BREATH = id("dragon_breath");
	@SinceMC("1.9")
	public static final Identifier SPLASH_POTION = id("splash_potion");
	@SinceMC("1.9")
	public static final Identifier SPECTRAL_ARROW = id("spectral_arrow");
	@SinceMC("1.9")
	public static final Identifier TIPPED_ARROW = id("tipped_arrow");
	@SinceMC("1.9")
	public static final Identifier LINGERING_POTION = id("lingering_potion");
	@SinceMC("1.9")
	public static final Identifier SHIELD = id("shield");
	@SinceMC("1.9")
	public static final Identifier ELYTRA = id("elytra");
	@SinceMC("1.9")
	public static final Identifier SPRUCE_BOAT = id("spruce_boat");
	@SinceMC("1.9")
	public static final Identifier BIRCH_BOAT = id("birch_boat");
	@SinceMC("1.9")
	public static final Identifier JUNGLE_BOAT = id("jungle_boat");
	@SinceMC("1.9")
	public static final Identifier ACACIA_BOAT = id("acacia_boat");
	@SinceMC("1.9")
	public static final Identifier DARK_OAK_BOAT = id("dark_oak_boat");
	@SinceMC("1.11")
	public static final Identifier TOTEM_OF_UNDYING = id("totem_of_undying");
	@SinceMC("1.11")
	public static final Identifier SHULKER_SHELL = id("shulker_shell");
	@SinceMC("1.11.1")
	public static final Identifier IRON_NUGGET = id("iron_nugget");
	@SinceMC("1.12")
	public static final Identifier KNOWLEDGE_BOOK = id("knowledge_book");
	public static final Identifier RECORD_13 = id("record_13");
	public static final Identifier RECORD_CAT = id("record_cat");
	public static final Identifier RECORD_BLOCKS = id("record_blocks");
	public static final Identifier RECORD_CHIRP = id("record_chirp");
	public static final Identifier RECORD_FAR = id("record_far");
	public static final Identifier RECORD_MALL = id("record_mall");
	public static final Identifier RECORD_MELLOHI = id("record_mellohi");
	public static final Identifier RECORD_STAL = id("record_stal");
	public static final Identifier RECORD_STRAD = id("record_strad");
	public static final Identifier RECORD_WARD = id("record_ward");
	public static final Identifier RECORD_11 = id("record_11");
	public static final Identifier RECORD_WAIT = id("record_wait");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
