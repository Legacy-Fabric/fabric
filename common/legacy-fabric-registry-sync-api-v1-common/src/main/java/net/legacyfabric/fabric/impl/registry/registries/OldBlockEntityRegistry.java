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

		map.put("Furnace", "minecraft:furnace");
		map.put("Chest", "minecraft:chest");
		map.put("EnderChest", "minecraft:ender_chest");
		map.put("RecordPlayer", "minecraft:jukebox");
		map.put("Trap", "minecraft:dispenser");
		map.put("Dropper", "minecraft:dropper");
		map.put("Sign", "minecraft:sign");
		map.put("MobSpawner", "minecraft:mob_spawner");
		map.put("Music", "minecraft:noteblock");
		map.put("Piston", "minecraft:piston");
		map.put("Cauldron", "minecraft:brewing_stand");
		map.put("EnchantTable", "minecraft:enchanting_table");
		map.put("Airportal", "minecraft:end_portal");
		map.put("Control", "minecraft:command_block");
		map.put("Beacon", "minecraft:beacon");
		map.put("Skull", "minecraft:skull");
		map.put("DLDetector", "minecraft:daylight_detector");
		map.put("Hopper", "minecraft:hopper");
		map.put("Comparator", "minecraft:comparator");
		map.put("FlowerPot", "minecraft:flower_pot");
		map.put("Banner", "minecraft:banner");
		map.put("Structure", "minecraft:structure_block");
		map.put("EndGateway", "minecraft:end_gateway");

		return map;
	}
}
