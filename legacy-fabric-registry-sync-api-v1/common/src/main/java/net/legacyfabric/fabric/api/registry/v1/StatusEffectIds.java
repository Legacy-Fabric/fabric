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

public class StatusEffectIds {
	public static final Identifier SPEED = id("speed");
	public static final Identifier SLOWNESS = id("slowness");
	public static final Identifier HASTE = id("haste");
	public static final Identifier MINING_FATIGUE = id("mining_fatigue");
	public static final Identifier STRENGTH = id("strength");
	public static final Identifier INSTANT_HEALTH = id("instant_health");
	public static final Identifier INSTANT_DAMAGE = id("instant_damage");
	public static final Identifier JUMP_BOOST = id("jump_boost");
	public static final Identifier NAUSEA = id("nausea");
	public static final Identifier REGENERATION = id("regeneration");
	public static final Identifier RESISTANCE = id("resistance");
	public static final Identifier FIRE_RESISTANCE = id("fire_resistance");
	public static final Identifier WATER_BREATHING = id("water_breathing");
	public static final Identifier INVISIBILITY = id("invisibility");
	public static final Identifier BLINDNESS = id("blindness");
	public static final Identifier NIGHT_VISION = id("night_vision");
	public static final Identifier HUNGER = id("hunger");
	public static final Identifier WEAKNESS = id("weakness");
	public static final Identifier POISON = id("poison");
	public static final Identifier WITHER = id("wither");
	public static final Identifier HEALTH_BOOST = id("health_boost");
	public static final Identifier ABSORPTION = id("absorption");
	public static final Identifier SATURATION = id("saturation");
	@SinceMC("1.9")
	public static final Identifier GLOWING = id("glowing");
	@SinceMC("1.9")
	public static final Identifier LEVITATION = id("levitation");
	@SinceMC("1.9")
	public static final Identifier LUCK = id("luck");
	@SinceMC("1.9")
	public static final Identifier BAD_LUCK = id("unluck");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
