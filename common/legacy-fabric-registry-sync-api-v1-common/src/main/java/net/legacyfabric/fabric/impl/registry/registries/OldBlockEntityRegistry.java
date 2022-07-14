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

package net.legacyfabric.fabric.impl.registry.registries;

import java.util.Map;

import net.minecraft.block.entity.BlockEntity;

import net.legacyfabric.fabric.api.registry.v1.BlockEntityIds;
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
	public Map<String, String> getRemapIdList() {
		Map<String, String> map = super.getRemapIdList();

		map.put("Furnace", BlockEntityIds.FURNACE.toString());
		map.put("Chest", BlockEntityIds.CHEST.toString());
		map.put("EnderChest", BlockEntityIds.ENDER_CHEST.toString());
		map.put("RecordPlayer", BlockEntityIds.JUKEBOX.toString());
		map.put("Trap", BlockEntityIds.DISPENSER.toString());
		map.put("Dropper", BlockEntityIds.DROPPER.toString());
		map.put("Sign", BlockEntityIds.SIGN.toString());
		map.put("MobSpawner", BlockEntityIds.MOB_SPAWNER.toString());
		map.put("Music", BlockEntityIds.NOTEBLOCK.toString());
		map.put("Piston", BlockEntityIds.PISTON.toString());
		map.put("Cauldron", BlockEntityIds.BREWING_STAND.toString());
		map.put("EnchantTable", BlockEntityIds.ENCHANTING_TABLE.toString());
		map.put("Airportal", BlockEntityIds.END_PORTAL.toString());
		map.put("Control", BlockEntityIds.COMMAND_BLOCK.toString());
		map.put("Beacon", BlockEntityIds.BEACON.toString());
		map.put("Skull", BlockEntityIds.SKULL.toString());
		map.put("DLDetector", BlockEntityIds.DAYLIGHT_DETECTOR.toString());
		map.put("Hopper", BlockEntityIds.HOPPER.toString());
		map.put("Comparator", BlockEntityIds.COMPARATOR.toString());
		map.put("FlowerPot", BlockEntityIds.FLOWER_POT.toString());
		map.put("Banner", BlockEntityIds.BANNER.toString());
		map.put("Structure", BlockEntityIds.STRUCTURE_BLOCK.toString());
		map.put("EndGateway", BlockEntityIds.END_GATEWAY.toString());

		return map;
	}

	@Override
	public Map<String, Integer> getRemapIdOrderList() {
		Map<String, Integer> map = super.getRemapIdOrderList();

		map.put("Furnace", 0);
		map.put("Chest", 1);
		map.put("EnderChest", 2);
		map.put("RecordPlayer", 3);
		map.put("Trap", 4);
		map.put("Dropper", 5);
		map.put("Sign", 6);
		map.put("MobSpawner", 7);
		map.put("Music", 8);
		map.put("Piston", 9);
		map.put("Cauldron", 10);
		map.put("EnchantTable", 11);
		map.put("Airportal", 12);
		map.put("Control", 13);
		map.put("Beacon", 14);
		map.put("Skull", 15);
		map.put("DLDetector", 16);
		map.put("Hopper", 17);
		map.put("Comparator", 18);
		map.put("FlowerPot", 19);
		map.put("Banner", 20);
		map.put("Structure", 21);
		map.put("EndGateway", 22);

		return map;
	}
}
