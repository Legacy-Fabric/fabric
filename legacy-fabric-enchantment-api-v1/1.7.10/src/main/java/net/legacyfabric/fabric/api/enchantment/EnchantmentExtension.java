package net.legacyfabric.fabric.api.enchantment;

import net.ornithemc.osl.registries.api.registry.Registry;

import net.minecraft.enchantment.Enchantment;

import net.legacyfabric.fabric.impl.enchantment.versioned.EnchantmentRegistryImpl;

public interface EnchantmentExtension {
	Registry<Enchantment> ENCHANTMENT_REGISTRY = EnchantmentRegistryImpl.REGISTRY;
	int REGISTRY_AUTO_ASSIGN_ID = -172;
}
