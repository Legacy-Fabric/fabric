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
}
