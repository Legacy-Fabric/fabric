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

package net.legacyfabric.fabric.api.enchantment;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;

public class EnchantmentIds {
	public static final Identifier PROTECTION = id("protection");
	public static final Identifier FIRE_PROTECTION = id("fire_protection");
	public static final Identifier FEATHER_FALLING = id("feather_falling");
	public static final Identifier BLAST_PROTECTION = id("blast_protection");
	public static final Identifier PROJECTILE_PROTECTION = id("projectile_protection");
	public static final Identifier RESPIRATION = id("respiration");
	public static final Identifier AQUA_AFFINITY = id("aqua_affinity");
	public static final Identifier THORNS = id("thorns");
	@SinceMC("1.8")
	public static final Identifier DEPTH_STRIDER = id("depth_strider");
	@SinceMC("1.9")
	public static final Identifier FROST_WALKER = id("frost_walker");
	@SinceMC("1.11")
	public static final Identifier CURSE_OF_BINDING = id("binding_curse");
	public static final Identifier SHARPNESS = id("sharpness");
	public static final Identifier SMITE = id("smite");
	public static final Identifier BANE_OF_ARTHROPODS = id("bane_of_arthropods");
	public static final Identifier KNOCKBACK = id("knockback");
	public static final Identifier FIRE_ASPECT = id("fire_aspect");
	public static final Identifier LOOTING = id("looting");
	@SinceMC("1.11.1")
	public static final Identifier SWEEPING_EDGE = id("sweeping");
	public static final Identifier EFFICIENCY = id("efficiency");
	public static final Identifier SILK_TOUCH = id("silk_touch");
	public static final Identifier UNBREAKING = id("unbreaking");
	public static final Identifier FORTUNE = id("fortune");
	public static final Identifier POWER = id("power");
	public static final Identifier PUNCH = id("punch");
	public static final Identifier FLAME = id("flame");
	public static final Identifier INFINITY = id("infinity");
	public static final Identifier LUCK_OF_THE_SEA = id("luck_of_the_sea");
	public static final Identifier LURE = id("lure");
	@SinceMC("1.9")
	public static final Identifier MENDING = id("mending");
	@SinceMC("1.11")
	public static final Identifier CURSE_OF_VANISHING = id("vanishing_curse");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
