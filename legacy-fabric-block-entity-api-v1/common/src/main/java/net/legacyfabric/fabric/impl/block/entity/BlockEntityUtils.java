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

package net.legacyfabric.fabric.impl.block.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.legacyfabric.fabric.api.block.entity.v1.BlockEntityTypeIds;
import net.legacyfabric.fabric.api.util.Identifier;

public class BlockEntityUtils {
	public static final BiMap<String, Identifier> OLD_TO_ID = HashBiMap.create();
	public static final BiMap<Identifier, String> ID_TO_OLD = OLD_TO_ID.inverse();

	static {
		OLD_TO_ID.put("Furnace", BlockEntityTypeIds.FURNACE);
		OLD_TO_ID.put("Chest", BlockEntityTypeIds.CHEST);
		OLD_TO_ID.put("EnderChest", BlockEntityTypeIds.ENDER_CHEST);
		OLD_TO_ID.put("RecordPlayer", BlockEntityTypeIds.JUKEBOX);
		OLD_TO_ID.put("Trap", BlockEntityTypeIds.DISPENSER);
		OLD_TO_ID.put("Dropper", BlockEntityTypeIds.DROPPER);
		OLD_TO_ID.put("Sign", BlockEntityTypeIds.SIGN);
		OLD_TO_ID.put("MobSpawner", BlockEntityTypeIds.MOB_SPAWNER);
		OLD_TO_ID.put("Music", BlockEntityTypeIds.NOTEBLOCK);
		OLD_TO_ID.put("Piston", BlockEntityTypeIds.PISTON);
		OLD_TO_ID.put("Cauldron", BlockEntityTypeIds.BREWING_STAND);
		OLD_TO_ID.put("EnchantTable", BlockEntityTypeIds.ENCHANTING_TABLE);
		OLD_TO_ID.put("Airportal", BlockEntityTypeIds.END_PORTAL);
		OLD_TO_ID.put("Control", BlockEntityTypeIds.COMMAND_BLOCK);
		OLD_TO_ID.put("Beacon", BlockEntityTypeIds.BEACON);
		OLD_TO_ID.put("Skull", BlockEntityTypeIds.SKULL);
		OLD_TO_ID.put("DLDetector", BlockEntityTypeIds.DAYLIGHT_DETECTOR);
		OLD_TO_ID.put("Hopper", BlockEntityTypeIds.HOPPER);
		OLD_TO_ID.put("Comparator", BlockEntityTypeIds.COMPARATOR);
		OLD_TO_ID.put("FlowerPot", BlockEntityTypeIds.FLOWER_POT);
		OLD_TO_ID.put("Banner", BlockEntityTypeIds.BANNER);
		OLD_TO_ID.put("Structure", BlockEntityTypeIds.STRUCTURE_BLOCK);
		OLD_TO_ID.put("EndGateway", BlockEntityTypeIds.END_GATEWAY);
	}
}
