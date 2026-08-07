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

package net.legacyfabric.fabric.impl.enchantment.versioned;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;

import net.minecraft.enchantment.Enchantment;

final class VanillaEnchantments {
	private static final String[] IDENTIFIERS = {
			"protection",
			"fire_protection",
			"feather_falling",
			"blast_protection",
			"projectile_protection",
			"respiration",
			"aqua_affinity",
			"thorns",
			"depth_strider",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"sharpness",
			"smite",
			"bane_of_arthropods",
			"knockback",
			"fire_aspect",
			"looting",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"efficiency",
			"silk_touch",
			"unbreaking",
			"fortune",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"power",
			"punch",
			"flame",
			"infinity",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"luck_of_the_sea",
			"lure"
	};

	static void init() {
		for (Field f : Enchantment.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Enchantment.class.isAssignableFrom(f.getType())) {
				try {
					Enchantment enchantment = (Enchantment) f.get(null);

					if (enchantment != null) {
						register(enchantment);
					}
				} catch (Throwable ignored) {
					// ignored
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void register(Enchantment enchantment) {
		if (enchantment.id >= 0 && enchantment.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[enchantment.id];

			if (identifier != null) {
				Registry.register(EnchantmentRegistryImpl.REGISTRY, enchantment.id, NamespacedIdentifiers.from(identifier), enchantment);
			}
		}
	}
}
