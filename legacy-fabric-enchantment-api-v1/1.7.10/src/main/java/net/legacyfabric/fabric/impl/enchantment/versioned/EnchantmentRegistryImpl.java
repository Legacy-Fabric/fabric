package net.legacyfabric.fabric.impl.enchantment.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.IdRegistry;

import net.legacyfabric.fabric.api.enchantment.EnchantmentEvents;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

public class EnchantmentRegistryImpl {
	public static final Registry<Enchantment> REGISTRY = VanillaRegistries.registerSimple(VanillaRegistryKeys.ENCHANTMENT, new IdRegistry<>());

	private static boolean locked = true;

	public static int getId(Enchantment enchantment) {
		return REGISTRY.getId(enchantment);
	}

	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return REGISTRY.getIdentifier(enchantment);
	}

	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return REGISTRY.getKey(enchantment);
	}

	public static Enchantment getEnchantment(int id) {
		return REGISTRY.get(id);
	}

	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Enchantment>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static <T extends Enchantment> T register(ResourceKey<Enchantment> identifier, T type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static void init() {
		SyncedRegistries.register(VanillaRegistryKeys.ENCHANTMENT);
		SyncedRegistries.registerFixer(VanillaRegistryKeys.ENCHANTMENT, NamespacedIdentifiers.from("enchantment/id"), new EnchantmentIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEnchantments() {
		VanillaEnchantments.init();
		EnchantmentEvents.REGISTER_ENCHANTMENTS.invoker().run();
	}
}
