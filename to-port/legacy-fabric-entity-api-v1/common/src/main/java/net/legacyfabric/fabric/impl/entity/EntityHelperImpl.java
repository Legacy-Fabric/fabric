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

package net.legacyfabric.fabric.impl.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.legacyfabric.fabric.api.entity.EntityTypeIds;
import net.legacyfabric.fabric.api.util.Identifier;

public class EntityHelperImpl {
	public static final BiMap<String, Identifier> NAME_TO_ID = HashBiMap.create();
	public static final BiMap<Identifier, String> ID_TO_NAME = NAME_TO_ID.inverse();

	static {
		NAME_TO_ID.put("Item", EntityTypeIds.ITEM);
		NAME_TO_ID.put("XPOrb", EntityTypeIds.XP_ORB);
		NAME_TO_ID.put("AreaEffectCloud", EntityTypeIds.AREA_EFFECT_CLOUD);
		NAME_TO_ID.put("ThrownEgg", EntityTypeIds.EGG);
		NAME_TO_ID.put("LeashKnot", EntityTypeIds.LEASH_KNOT);
		NAME_TO_ID.put("Painting", EntityTypeIds.PAINTING);
		NAME_TO_ID.put("Arrow", EntityTypeIds.ARROW);
		NAME_TO_ID.put("Snowball", EntityTypeIds.SNOWBALL);
		NAME_TO_ID.put("Fireball", EntityTypeIds.FIREBALL);
		NAME_TO_ID.put("SmallFireball", EntityTypeIds.SMALL_FIREBALL);
		NAME_TO_ID.put("ThrownEnderpearl", EntityTypeIds.ENDER_PEARL);
		NAME_TO_ID.put("EyeOfEnderSignal", EntityTypeIds.EYE_OF_ENDER);
		NAME_TO_ID.put("ThrownPotion", EntityTypeIds.POTION);
		NAME_TO_ID.put("ThrownExpBottle", EntityTypeIds.XP_BOTTLE);
		NAME_TO_ID.put("ItemFrame", EntityTypeIds.ITEM_FRAME);
		NAME_TO_ID.put("WitherSkull", EntityTypeIds.WITHER_SKULL);
		NAME_TO_ID.put("PrimedTnt", EntityTypeIds.TNT);
		NAME_TO_ID.put("FallingSand", EntityTypeIds.FALLING_BLOCK);
		NAME_TO_ID.put("FireworksRocketEntity", EntityTypeIds.FIREWORKS_ROCKET);
		NAME_TO_ID.put("SpectralArrow", EntityTypeIds.SPECTRAL_ARROW);
		NAME_TO_ID.put("ShulkerBullet", EntityTypeIds.SHULKER_BULLET);
		NAME_TO_ID.put("DragonFireball", EntityTypeIds.DRAGON_FIREBALL);
		NAME_TO_ID.put("ArmorStand", EntityTypeIds.ARMOR_STAND);
		NAME_TO_ID.put("MinecartCommandBlock", EntityTypeIds.COMMAND_BLOCK_MINECART);
		NAME_TO_ID.put("Boat", EntityTypeIds.BOAT);
		NAME_TO_ID.put("MinecartRideable", EntityTypeIds.MINECART);
		NAME_TO_ID.put("MinecartChest", EntityTypeIds.CHEST_MINECART);
		NAME_TO_ID.put("MinecartFurnace", EntityTypeIds.FURNACE_MINECART);
		NAME_TO_ID.put("MinecartTNT", EntityTypeIds.TNT_MINECART);
		NAME_TO_ID.put("MinecartHopper", EntityTypeIds.HOPPER_MINECART);
		NAME_TO_ID.put("MinecartSpawner", EntityTypeIds.SPAWNER_MINECART);
		NAME_TO_ID.put("Mob", EntityTypeIds.MOB);
		NAME_TO_ID.put("Monster", EntityTypeIds.MONSTER);
		NAME_TO_ID.put("Creeper", EntityTypeIds.CREEPER);
		NAME_TO_ID.put("Skeleton", EntityTypeIds.SKELETON);
		NAME_TO_ID.put("Spider", EntityTypeIds.SPIDER);
		NAME_TO_ID.put("Giant", EntityTypeIds.GIANT);
		NAME_TO_ID.put("Zombie", EntityTypeIds.ZOMBIE);
		NAME_TO_ID.put("Slime", EntityTypeIds.SLIME);
		NAME_TO_ID.put("Ghast", EntityTypeIds.GHAST);
		NAME_TO_ID.put("PigZombie", EntityTypeIds.ZOMBIE_PIGMAN);
		NAME_TO_ID.put("Enderman", EntityTypeIds.ENDERMAN);
		NAME_TO_ID.put("CaveSpider", EntityTypeIds.CAVE_SPIDER);
		NAME_TO_ID.put("Silverfish", EntityTypeIds.SILVERFISH);
		NAME_TO_ID.put("Blaze", EntityTypeIds.BLAZE);
		NAME_TO_ID.put("LavaSlime", EntityTypeIds.MAGMA_CUBE);
		NAME_TO_ID.put("EnderDragon", EntityTypeIds.ENDER_DRAGON);
		NAME_TO_ID.put("WitherBoss", EntityTypeIds.WITHER_BOSS);
		NAME_TO_ID.put("Bat", EntityTypeIds.BAT);
		NAME_TO_ID.put("Witch", EntityTypeIds.WITCH);
		NAME_TO_ID.put("Endermite", EntityTypeIds.ENDERMITE);
		NAME_TO_ID.put("Guardian", EntityTypeIds.GUARDIAN);
		NAME_TO_ID.put("Shulker", EntityTypeIds.SHULKER);
		NAME_TO_ID.put("Pig", EntityTypeIds.PIG);
		NAME_TO_ID.put("Sheep", EntityTypeIds.SHEEP);
		NAME_TO_ID.put("Cow", EntityTypeIds.COW);
		NAME_TO_ID.put("Chicken", EntityTypeIds.CHICKEN);
		NAME_TO_ID.put("Squid", EntityTypeIds.SQUID);
		NAME_TO_ID.put("Wolf", EntityTypeIds.WOLF);
		NAME_TO_ID.put("MushroomCow", EntityTypeIds.MOOSHROOM);
		NAME_TO_ID.put("SnowMan", EntityTypeIds.SNOWMAN);
		NAME_TO_ID.put("Ozelot", EntityTypeIds.OCELOT);
		NAME_TO_ID.put("VillagerGolem", EntityTypeIds.IRON_GOLEM);
		NAME_TO_ID.put("EntityHorse", EntityTypeIds.HORSE);
		NAME_TO_ID.put("Rabbit", EntityTypeIds.RABBIT);
		NAME_TO_ID.put("PolarBear", EntityTypeIds.POLAR_BEAR);
		NAME_TO_ID.put("Villager", EntityTypeIds.VILLAGER);
		NAME_TO_ID.put("EnderCrystal", EntityTypeIds.ENDER_CRYSTAL);
		NAME_TO_ID.put("Player", EntityTypeIds.PLAYER);
		NAME_TO_ID.put("LightningBolt", EntityTypeIds.LIGHTNING_BOLT);
	}
}
