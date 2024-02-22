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

package net.legacyfabric.fabric.impl.registry.registries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.Entity;
import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.api.registry.v1.EntityTypeIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.util.OldRemappedRegistry;

public class OldEntityTypeRegistry extends OldRemappedRegistry<String, Class<? extends Entity>> {
	private final BiMap<String, Class<? extends Entity>> stringClassBiMap;
	private final BiMap<Integer, Class<? extends Entity>> integerClassBiMap;
	private final BiMap<String, Integer> stringIntegerBiMap;

	private IdListCompat<Class<? extends Entity>> IDLIST = (IdListCompat<Class<? extends Entity>>) new IdList<Class<? extends Entity>>();

	public OldEntityTypeRegistry(BiMap<String, Class<? extends Entity>> stringClassBiMap,
									BiMap<Integer, Class<? extends Entity>> integerClassBiMap,
									BiMap<String, Integer> stringIntegerBiMap) {
		this.stringClassBiMap = stringClassBiMap;
		this.integerClassBiMap = integerClassBiMap;
		this.stringIntegerBiMap = stringIntegerBiMap;

		this.remapDefaultIds();
	}

	@Override
	public void remapDefaultIds() {
		List<String> list = new ArrayList<>();

		for (Map.Entry<String, Class<? extends Entity>> entry : this.stringClassBiMap.entrySet()) {
			if (!this.getNewKey(entry.getKey()).equals(entry.getKey())) {
				list.add(entry.getKey());
			}
		}

		for (String oldKey : list) {
			if (this.getNewKey(oldKey) == null) continue;

			this.stringClassBiMap.put(this.getNewKey(oldKey), this.stringClassBiMap.remove(oldKey));
			this.stringIntegerBiMap.put(this.getNewKey(oldKey), this.stringIntegerBiMap.remove(oldKey));
		}

		this.stringIntegerBiMap.remove(null);

		for (Map.Entry<Integer, Class<? extends Entity>> entry : this.integerClassBiMap.entrySet()) {
			this.IDLIST.setValue(entry.getValue(), entry.getKey());
		}
	}

	@Override
	public BiMap<String, String> generateOldToNewKeyMap() {
		BiMap<String, String> map = super.generateOldToNewKeyMap();

		map.put("Item", EntityTypeIds.ITEM.toString());
		map.put("XPOrb", EntityTypeIds.XP_ORB.toString());
		map.put("AreaEffectCloud", EntityTypeIds.AREA_EFFECT_CLOUD.toString());
		map.put("ThrownEgg", EntityTypeIds.EGG.toString());
		map.put("LeashKnot", EntityTypeIds.LEASH_KNOT.toString());
		map.put("Painting", EntityTypeIds.PAINTING.toString());
		map.put("Arrow", EntityTypeIds.ARROW.toString());
		map.put("Snowball", EntityTypeIds.SNOWBALL.toString());
		map.put("Fireball", EntityTypeIds.FIREBALL.toString());
		map.put("SmallFireball", EntityTypeIds.SMALL_FIREBALL.toString());
		map.put("ThrownEnderpearl", EntityTypeIds.ENDER_PEARL.toString());
		map.put("EyeOfEnderSignal", EntityTypeIds.EYE_OF_ENDER.toString());
		map.put("ThrownPotion", EntityTypeIds.POTION.toString());
		map.put("ThrownExpBottle", EntityTypeIds.XP_BOTTLE.toString());
		map.put("ItemFrame", EntityTypeIds.ITEM_FRAME.toString());
		map.put("WitherSkull", EntityTypeIds.WITHER_SKULL.toString());
		map.put("PrimedTnt", EntityTypeIds.TNT.toString());
		map.put("FallingSand", EntityTypeIds.FALLING_BLOCK.toString());
		map.put("FireworksRocketEntity", EntityTypeIds.FIREWORKS_ROCKET.toString());
		map.put("SpectralArrow", EntityTypeIds.SPECTRAL_ARROW.toString());
		map.put("ShulkerBullet", EntityTypeIds.SHULKER_BULLET.toString());
		map.put("DragonFireball", EntityTypeIds.DRAGON_FIREBALL.toString());
		map.put("ArmorStand", EntityTypeIds.ARMOR_STAND.toString());
		map.put("MinecartCommandBlock", EntityTypeIds.COMMAND_BLOCK_MINECART.toString());
		map.put("Boat", EntityTypeIds.BOAT.toString());
		map.put("MinecartRideable", EntityTypeIds.MINECART.toString());
		map.put("MinecartChest", EntityTypeIds.CHEST_MINECART.toString());
		map.put("MinecartFurnace", EntityTypeIds.FURNACE_MINECART.toString());
		map.put("MinecartTNT", EntityTypeIds.TNT_MINECART.toString());
		map.put("MinecartHopper", EntityTypeIds.HOPPER_MINECART.toString());
		map.put("MinecartSpawner", EntityTypeIds.SPAWNER_MINECART.toString());
		map.put("Mob", EntityTypeIds.MOB.toString());
		map.put("Monster", EntityTypeIds.MONSTER.toString());
		map.put("Creeper", EntityTypeIds.CREEPER.toString());
		map.put("Skeleton", EntityTypeIds.SKELETON.toString());
		map.put("Spider", EntityTypeIds.SPIDER.toString());
		map.put("Giant", EntityTypeIds.GIANT.toString());
		map.put("Zombie", EntityTypeIds.ZOMBIE.toString());
		map.put("Slime", EntityTypeIds.SLIME.toString());
		map.put("Ghast", EntityTypeIds.GHAST.toString());
		map.put("PigZombie", EntityTypeIds.ZOMBIE_PIGMAN.toString());
		map.put("Enderman", EntityTypeIds.ENDERMAN.toString());
		map.put("CaveSpider", EntityTypeIds.CAVE_SPIDER.toString());
		map.put("Silverfish", EntityTypeIds.SILVERFISH.toString());
		map.put("Blaze", EntityTypeIds.BLAZE.toString());
		map.put("LavaSlime", EntityTypeIds.MAGMA_CUBE.toString());
		map.put("EnderDragon", EntityTypeIds.ENDER_DRAGON.toString());
		map.put("WitherBoss", EntityTypeIds.WITHER_BOSS.toString());
		map.put("Bat", EntityTypeIds.BAT.toString());
		map.put("Witch", EntityTypeIds.WITCH.toString());
		map.put("Endermite", EntityTypeIds.ENDERMITE.toString());
		map.put("Guardian", EntityTypeIds.GUARDIAN.toString());
		map.put("Shulker", EntityTypeIds.SHULKER.toString());
		map.put("Pig", EntityTypeIds.PIG.toString());
		map.put("Sheep", EntityTypeIds.SHEEP.toString());
		map.put("Cow", EntityTypeIds.COW.toString());
		map.put("Chicken", EntityTypeIds.CHICKEN.toString());
		map.put("Squid", EntityTypeIds.SQUID.toString());
		map.put("Wolf", EntityTypeIds.WOLF.toString());
		map.put("MushroomCow", EntityTypeIds.MOOSHROOM.toString());
		map.put("SnowMan", EntityTypeIds.SNOWMAN.toString());
		map.put("Ozelot", EntityTypeIds.OCELOT.toString());
		map.put("VillagerGolem", EntityTypeIds.IRON_GOLEM.toString());
		map.put("EntityHorse", EntityTypeIds.HORSE.toString());
		map.put("Rabbit", EntityTypeIds.RABBIT.toString());
		map.put("PolarBear", EntityTypeIds.POLAR_BEAR.toString());
		map.put("Villager", EntityTypeIds.VILLAGER.toString());
		map.put("EnderCrystal", EntityTypeIds.ENDER_CRYSTAL.toString());
		map.put("Player", EntityTypeIds.PLAYER.toString());
		map.put("LightningBolt", EntityTypeIds.LIGHTNING_BOLT.toString());

		return map;
	}

	@Override
	public IdListCompat<Class<? extends Entity>> getIds() {
		return IDLIST;
	}

	@Override
	public Map<Class<? extends Entity>, String> getObjects() {
		return this.stringClassBiMap.inverse();
	}

	@Override
	public void setIds(IdListCompat<Class<? extends Entity>> idList) {
		this.IDLIST = idList;

		BiMap<Class<? extends Entity>, Integer> inversedMap = this.integerClassBiMap.inverse();
		BiMap<Class<? extends Entity>, String> inversedMap2 = this.stringClassBiMap.inverse();

		for (Class<? extends Entity> entityType : this.IDLIST.getList()) {
			inversedMap.put(entityType, this.IDLIST.getInt(entityType));

			this.stringIntegerBiMap.put(inversedMap2.get(entityType), this.IDLIST.getInt(entityType));
		}

		this.stringIntegerBiMap.remove(null);
	}

	@Override
	public IdListCompat<Class<? extends Entity>> createIdList() {
		return (IdListCompat<Class<? extends Entity>>) new IdList<Class<? extends Entity>>();
	}

	@Override
	public int getRawID(Class<? extends Entity> object) {
		return IDLIST.getInt(object);
	}

	@Override
	public String getKey(Class<? extends Entity> object) {
		return this.stringClassBiMap.inverse().get(object);
	}

	@Override
	public Class<? extends Entity> getValue(Object key) {
		return this.stringClassBiMap.get(this.toKeyType(key));
	}

	@Override
	public Class<? extends Entity> register(int i, Object key, Class<? extends Entity> value) {
		String nativeKey = this.toKeyType(key);

		this.stringClassBiMap.put(nativeKey, value);
		this.integerClassBiMap.put(i, value);
		this.stringIntegerBiMap.put(nativeKey, i);

		this.IDLIST.setValue(value, i);
		this.getEventHolder().getAddEvent().invoker().onEntryAdded(i, new Identifier(key), value);
		return value;
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.JAVA;
	}

	@NotNull
	@Override
	public Iterator<Class<? extends Entity>> iterator() {
		return this.IDLIST.iterator();
	}
}
