package net.legacyfabric.fabric.test.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffectInstance;

public class TestEnchantment extends Enchantment {
	protected TestEnchantment() {
		super(REGISTRY_AUTO_ASSIGN_ID, 2, EnchantmentCategory.ARMOR_FEET);
	}

	@Override
	public void applyDamageWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT.id, 50, 10));
	}

	@Override
	public void applyProtectionWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT.id, 50, 10));
	}
}
