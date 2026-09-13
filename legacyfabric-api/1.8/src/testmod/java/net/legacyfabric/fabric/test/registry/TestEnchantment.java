package net.legacyfabric.fabric.test.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public class TestEnchantment extends Enchantment {
	protected TestEnchantment(NamespacedIdentifier identifier) {
		super(REGISTRY_AUTO_ASSIGN_ID, new Identifier(identifier.toString()), 2, EnchantmentCategory.ARMOR_FEET);
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
