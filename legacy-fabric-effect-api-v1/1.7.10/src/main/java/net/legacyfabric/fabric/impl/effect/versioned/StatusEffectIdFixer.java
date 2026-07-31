package net.legacyfabric.fabric.impl.effect.versioned;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

import net.minecraft.entity.living.effect.StatusEffect;

public class StatusEffectIdFixer implements IdFixer {
	@Override
	public void apply() {
		for (int id = 0; id < StatusEffect.BY_ID.length; id++) {
			StatusEffect effect = StatusEffect.BY_ID[id];

			if (effect != null) {
				effect.id = id;
			}
		}
	}
}
