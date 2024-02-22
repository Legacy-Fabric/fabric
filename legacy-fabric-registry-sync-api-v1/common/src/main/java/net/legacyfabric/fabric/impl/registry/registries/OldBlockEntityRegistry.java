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

import java.util.Map;

import com.google.common.collect.BiMap;

import net.minecraft.block.entity.BlockEntity;

import net.legacyfabric.fabric.api.registry.v1.BlockEntityTypeIds;
import net.legacyfabric.fabric.impl.registry.util.MapBasedRegistry;

public class OldBlockEntityRegistry extends MapBasedRegistry<String, Class<? extends BlockEntity>> {
	public OldBlockEntityRegistry(Map<String, Class<? extends BlockEntity>> defaultMap, Map<Class<? extends BlockEntity>, String> invertedMap) {
		super(defaultMap, invertedMap);
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.JAVA;
	}

	@Override
	public BiMap<String, String> generateOldToNewKeyMap() {
		BiMap<String, String> map = super.generateOldToNewKeyMap();

		map.put("Furnace", BlockEntityTypeIds.FURNACE.toString());
		map.put("Chest", BlockEntityTypeIds.CHEST.toString());
		map.put("EnderChest", BlockEntityTypeIds.ENDER_CHEST.toString());
		map.put("RecordPlayer", BlockEntityTypeIds.JUKEBOX.toString());
		map.put("Trap", BlockEntityTypeIds.DISPENSER.toString());
		map.put("Dropper", BlockEntityTypeIds.DROPPER.toString());
		map.put("Sign", BlockEntityTypeIds.SIGN.toString());
		map.put("MobSpawner", BlockEntityTypeIds.MOB_SPAWNER.toString());
		map.put("Music", BlockEntityTypeIds.NOTEBLOCK.toString());
		map.put("Piston", BlockEntityTypeIds.PISTON.toString());
		map.put("Cauldron", BlockEntityTypeIds.BREWING_STAND.toString());
		map.put("EnchantTable", BlockEntityTypeIds.ENCHANTING_TABLE.toString());
		map.put("Airportal", BlockEntityTypeIds.END_PORTAL.toString());
		map.put("Control", BlockEntityTypeIds.COMMAND_BLOCK.toString());
		map.put("Beacon", BlockEntityTypeIds.BEACON.toString());
		map.put("Skull", BlockEntityTypeIds.SKULL.toString());
		map.put("DLDetector", BlockEntityTypeIds.DAYLIGHT_DETECTOR.toString());
		map.put("Hopper", BlockEntityTypeIds.HOPPER.toString());
		map.put("Comparator", BlockEntityTypeIds.COMPARATOR.toString());
		map.put("FlowerPot", BlockEntityTypeIds.FLOWER_POT.toString());
		map.put("Banner", BlockEntityTypeIds.BANNER.toString());
		map.put("Structure", BlockEntityTypeIds.STRUCTURE_BLOCK.toString());
		map.put("EndGateway", BlockEntityTypeIds.END_GATEWAY.toString());

		return map;
	}

	@Override
	public BiMap<Integer, String> generateIdToKeyMap() {
		BiMap<Integer, String> map = super.generateIdToKeyMap();

		map.put(0, "Furnace");
		map.put(1, "Chest");
		map.put(2, "EnderChest");
		map.put(3, "RecordPlayer");
		map.put(4, "Trap");
		map.put(5, "Dropper");
		map.put(6, "Sign");
		map.put(7, "MobSpawner");
		map.put(8, "Music");
		map.put(9, "Piston");
		map.put(10, "Cauldron");
		map.put(11, "EnchantTable");
		map.put(12, "Airportal");
		map.put(13, "Control");
		map.put(14, "Beacon");
		map.put(15, "Skull");
		map.put(16, "DLDetector");
		map.put(17, "Hopper");
		map.put(18, "Comparator");
		map.put(19, "FlowerPot");
		map.put(20, "Banner");
		map.put(21, "Structure");
		map.put(22, "EndGateway");

		return map;
	}
}
