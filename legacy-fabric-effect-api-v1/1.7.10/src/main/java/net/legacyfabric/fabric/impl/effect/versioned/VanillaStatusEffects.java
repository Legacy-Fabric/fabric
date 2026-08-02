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
				StatusEffectRegistryImpl.register(effect.id, NamespacedIdentifiers.from(identifier), effect);
			}
		}
	}
}
