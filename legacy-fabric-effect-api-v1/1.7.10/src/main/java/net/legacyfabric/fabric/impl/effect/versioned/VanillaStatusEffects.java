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

package net.legacyfabric.fabric.impl.effect.versioned;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

import net.minecraft.entity.living.effect.StatusEffect;

final class VanillaStatusEffects {
	private static final String[] IDENTIFIERS = {
			null,

			"speed",
			"slowness",
			"haste",
			"mining_fatigue",
			"strength",
			"instant_health",
			"instant_damage",
			"jump_boost",
			"nausea",
			"regeneration",
			"resistance",
			"fire_resistance",
			"water_breathing",
			"invisibility",
			"blindness",
			"night_vision",
			"hunger",
			"weakness",
			"poison",
			"wither",
			"health_boost",
			"absorption",
			"saturation"
	};

	static void init() {
		for (Field f : StatusEffect.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && StatusEffect.class.isAssignableFrom(f.getType())) {
				try {
					StatusEffect effect = (StatusEffect) f.get(null);

					if (effect != null) {
						register(effect);
					}
				} catch (Throwable ignored) {
					// ignored
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void register(StatusEffect effect) {
		if (effect.id >= 0 && effect.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[effect.id];

			if (identifier != null) {
				StatusEffectRegistryImpl.register(NamespacedIdentifiers.from(identifier), effect);
			}
		}
	}
}
