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

package net.legacyfabric.fabric.api.registry.v1;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;

public class BlockEntityIds {
	public static final Identifier FURNACE = id("furnace");
	public static final Identifier CHEST = id("chest");
	public static final Identifier ENDER_CHEST = id("ender_chest");
	public static final Identifier JUKEBOX = id("jukebox");
	public static final Identifier DISPENSER = id("dispenser");
	public static final Identifier DROPPER = id("dropper");
	public static final Identifier SIGN = id("sign");
	public static final Identifier MOB_SPAWNER = id("mob_spawner");
	public static final Identifier NOTEBLOCK = id("noteblock");
	public static final Identifier PISTON = id("piston");
	public static final Identifier BREWING_STAND = id("brewing_stand");
	public static final Identifier ENCHANTING_TABLE = id("enchanting_table");
	public static final Identifier END_PORTAL = id("end_portal");
	public static final Identifier BEACON = id("beacon");
	public static final Identifier SKULL = id("skull");
	public static final Identifier DAYLIGHT_DETECTOR = id("daylight_detector");
	public static final Identifier HOPPER = id("hopper");
	public static final Identifier COMPARATOR = id("comparator");
	public static final Identifier FLOWER_POT = id("flower_pot");
	@SinceMC("1.8")
	public static final Identifier BANNER = id("banner");
	@SinceMC("1.9.4")
	public static final Identifier STRUCTURE_BLOCK = id("structure_block");
	@SinceMC("1.9.4")
	public static final Identifier END_GATEWAY = id("end_gateway");
	public static final Identifier COMMAND_BLOCK = id("command_block");
	@SinceMC("1.11.2")
	public static final Identifier SHULKER_BOX = id("shulker_box");
	@SinceMC("1.12.2")
	public static final Identifier BED = id("bed");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
