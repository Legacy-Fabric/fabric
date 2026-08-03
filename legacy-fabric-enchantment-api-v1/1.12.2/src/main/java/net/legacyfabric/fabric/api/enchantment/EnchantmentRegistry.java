package net.legacyfabric.fabric.api.enchantment;

import java.util.Set;

import net.legacyfabric.fabric.impl.enchantment.versioned.EnchantmentRegistryImpl;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public final class EnchantmentRegistry {
	public static final Registry<Enchantment> REGISTRY = EnchantmentRegistryImpl.REGISTRY;

	public static int getId(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getId(enchantment);
	}

	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getIdentifier(enchantment);
	}

	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getKey(enchantment);
	}

	public static Enchantment getEnchantment(int id) {
		return EnchantmentRegistryImpl.getEnchantment(id);
	}

	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return EnchantmentRegistryImpl.getEnchantment(identifier);
	}

	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return EnchantmentRegistryImpl.getEnchantment(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return EnchantmentRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<Enchantment>> keySet() {
		return EnchantmentRegistryImpl.keySet();
	}

	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T type) {
		return EnchantmentRegistryImpl.register(identifier, type);
	}

	public static <T extends Enchantment> T register(ResourceKey<Enchantment> key, T type) {
		return EnchantmentRegistryImpl.register(key, type);
	}
}
