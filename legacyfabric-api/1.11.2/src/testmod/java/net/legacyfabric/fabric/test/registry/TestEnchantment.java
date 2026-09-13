package net.legacyfabric.fabric.test.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffectInstance;

public class TestEnchantment extends Enchantment {
	protected TestEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
	}

	@Override
	public void applyDamageWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT, 50, 10));
	}

	@Override
	public void applyProtectionWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT, 50, 10));
	}
}
