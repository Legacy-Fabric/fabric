package net.legacyfabric.fabric.test.registry;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffect;

public class TestStatusEffect extends StatusEffect {
	public TestStatusEffect(boolean bl, int j) {
		super(REGISTRY_AUTO_ASSIGN_ID, bl, j);
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
