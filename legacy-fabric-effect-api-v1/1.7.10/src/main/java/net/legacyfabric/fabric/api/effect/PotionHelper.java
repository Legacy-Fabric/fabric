package net.legacyfabric.fabric.api.effect;

import net.legacyfabric.fabric.impl.effect.versioned.PotionHelperImpl;

import net.minecraft.entity.effect.StatusEffect;

public interface PotionHelper {

	static void registerLevels(StatusEffect effect, String levels) {
		PotionHelperImpl.registerLevels(effect, levels);
	}

	static void registerAmplifyingFactor(StatusEffect effect, String amplifyingFactor) {
		PotionHelperImpl.registerAmplifyingFactor(effect, amplifyingFactor);
	}
}
