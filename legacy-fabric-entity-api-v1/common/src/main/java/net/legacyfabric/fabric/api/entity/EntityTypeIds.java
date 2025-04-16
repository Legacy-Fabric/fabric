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

package net.legacyfabric.fabric.api.entity;

import net.legacyfabric.fabric.api.util.BeforeMC;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;

public class EntityTypeIds {
	public static final Identifier ITEM = id("item");
	public static final Identifier XP_ORB = id("xp_orb");
	@SinceMC("1.9")
	public static final Identifier AREA_EFFECT_CLOUD = id("area_effect_cloud");
	@SinceMC("1.11")
	public static final Identifier ELDER_GUARDIAN = id("elder_guardian");
	@SinceMC("1.11")
	public static final Identifier WITHER_SKELETON = id("wither_skeleton");
	@SinceMC("1.11")
	public static final Identifier STRAY = id("stray");
	@SinceMC("1.8.9")
	public static final Identifier EGG = id("egg");
	public static final Identifier LEASH_KNOT = id("leash_knot");
	public static final Identifier PAINTING = id("painting");
	public static final Identifier ARROW = id("arrow");
	public static final Identifier SNOWBALL = id("snowball");
	public static final Identifier FIREBALL = id("fireball");
	public static final Identifier SMALL_FIREBALL = id("small_fireball");
	public static final Identifier ENDER_PEARL = id("ender_pearl");
	public static final Identifier EYE_OF_ENDER = id("eye_of_ender_signal");
	public static final Identifier POTION = id("potion");
	public static final Identifier XP_BOTTLE = id("xp_bottle");
	public static final Identifier ITEM_FRAME = id("item_frame");
	public static final Identifier WITHER_SKULL = id("wither_skull");
	public static final Identifier TNT = id("tnt");
	public static final Identifier FALLING_BLOCK = id("falling_block");
	public static final Identifier FIREWORKS_ROCKET = id("fireworks_rocket");
	@SinceMC("1.11")
	public static final Identifier HUSK = id("husk");
	@SinceMC("1.9")
	public static final Identifier SPECTRAL_ARROW = id("spectral_arrow");
	@SinceMC("1.9")
	public static final Identifier SHULKER_BULLET = id("shulker_bullet");
	@SinceMC("1.9")
	public static final Identifier DRAGON_FIREBALL = id("dragon_fireball");
	@SinceMC("1.11")
	public static final Identifier ZOMBIE_VILLAGER = id("zombie_villager");
	@SinceMC("1.11")
	public static final Identifier SKELETON_HORSE = id("skeleton_horse");
	@SinceMC("1.11")
	public static final Identifier ZOMBIE_HORSE = id("zombie_horse");
	@SinceMC("1.8")
	public static final Identifier ARMOR_STAND = id("armor_stand");
	@SinceMC("1.11")
	public static final Identifier DONKEY = id("donkey");
	@SinceMC("1.11")
	public static final Identifier MULE = id("mule");
	@SinceMC("1.11")
	public static final Identifier EVOCATOR_FANGS = id("evocation_fangs");
	@SinceMC("1.11")
	public static final Identifier EVOCATOR = id("evocation_illager");
	@SinceMC("1.11")
	public static final Identifier VEX = id("vex");
	@SinceMC("1.11")
	public static final Identifier VINDICATOR = id("vindication_illager");
	@SinceMC("1.12")
	public static final Identifier ILLUSIONER = id("illusion_illager");
	public static final Identifier COMMAND_BLOCK_MINECART = id("commandblock_minecart");
	public static final Identifier BOAT = id("boat");
	public static final Identifier MINECART = id("minecart");
	public static final Identifier CHEST_MINECART = id("chest_minecart");
	public static final Identifier FURNACE_MINECART = id("furnace_minecart");
	public static final Identifier TNT_MINECART = id("tnt_minecart");
	public static final Identifier HOPPER_MINECART = id("hopper_minecart");
	public static final Identifier SPAWNER_MINECART = id("spawner_minecart");
	@BeforeMC("1.11")
	public static final Identifier MOB = id("mod");
	@BeforeMC("1.11")
	public static final Identifier MONSTER = id("monster");
	public static final Identifier CREEPER = id("creeper");
	public static final Identifier SKELETON = id("skeleton");
	public static final Identifier SPIDER = id("spider");
	public static final Identifier GIANT = id("giant");
	public static final Identifier ZOMBIE = id("zombie");
	public static final Identifier SLIME = id("slime");
	public static final Identifier GHAST = id("ghast");
	public static final Identifier ZOMBIE_PIGMAN = id("zombie_pigman");
	public static final Identifier ENDERMAN = id("enderman");
	public static final Identifier CAVE_SPIDER = id("cave_spider");
	public static final Identifier SILVERFISH = id("silverfish");
	public static final Identifier BLAZE = id("blaze");
	public static final Identifier MAGMA_CUBE = id("magma_cube");
	public static final Identifier ENDER_DRAGON = id("ender_dragon");
	public static final Identifier WITHER_BOSS = id("wither");
	public static final Identifier BAT = id("bat");
	public static final Identifier WITCH = id("witch");
	@SinceMC("1.8")
	public static final Identifier ENDERMITE = id("endermite");
	@SinceMC("1.8")
	public static final Identifier GUARDIAN = id("guardian");
	@SinceMC("1.9")
	public static final Identifier SHULKER = id("shulker");
	public static final Identifier PIG = id("pig");
	public static final Identifier SHEEP = id("sheep");
	public static final Identifier COW = id("cow");
	public static final Identifier CHICKEN = id("chicken");
	public static final Identifier SQUID = id("squid");
	public static final Identifier WOLF = id("wolf");
	public static final Identifier MOOSHROOM = id("mooshroom");
	public static final Identifier SNOWMAN = id("snowman");
	public static final Identifier OCELOT = id("ocelot");
	public static final Identifier IRON_GOLEM = id("villager_golem");
	public static final Identifier HORSE = id("horse");
	@SinceMC("1.8")
	public static final Identifier RABBIT = id("rabbit");
	@SinceMC("1.10")
	public static final Identifier POLAR_BEAR = id("polar_bear");
	@SinceMC("1.11")
	public static final Identifier LLAMA = id("llama");
	@SinceMC("1.11")
	public static final Identifier LLAMA_SPIT = id("llama_spit");
	@SinceMC("1.12")
	public static final Identifier PARROT = id("parrot");
	public static final Identifier VILLAGER = id("villager");
	public static final Identifier ENDER_CRYSTAL = id("ender_crystal");
	public static final Identifier LIGHTNING_BOLT = id("lightning_bolt");
	public static final Identifier PLAYER = id("player");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
