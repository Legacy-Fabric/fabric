package net.legacyfabric.fabric.impl.enchantment.versioned;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

import net.minecraft.entity.living.effect.StatusEffect;

public class EnchantmentIdFixer implements IdFixer {
	@Override
	public void apply() {
		for (int id = 0; id < Enchantment.BY_ID.length; id++) {
			Enchantment enchantment = Enchantment.BY_ID[id];

			if (enchantment != null) {
				enchantment.id = id;
			}
		}
	}
}
