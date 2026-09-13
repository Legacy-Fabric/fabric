package net.legacyfabric.fabric.test.registry;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public class TestStatusEffect extends StatusEffect {
	public TestStatusEffect(NamespacedIdentifier identifier, boolean bl, int j) {
		super(REGISTRY_AUTO_ASSIGN_ID, new Identifier(identifier.toString()), bl, j);
	}

	@Override
	public void apply(LivingEntity livingEntity, int i) {
		if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
			livingEntity.heal(1.0F);
		}
	}

	@Override
	public boolean shouldApply(int duration, int amplifier) {
		int i;

		i = 50 >> amplifier;

		if (i > 0) {
			return duration % i == 0;
		} else {
			return true;
		}
	}
}
