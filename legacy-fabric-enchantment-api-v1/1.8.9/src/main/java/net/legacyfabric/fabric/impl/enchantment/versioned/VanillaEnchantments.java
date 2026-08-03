package net.legacyfabric.fabric.impl.enchantment.versioned;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.registries.api.registry.Registry;

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
