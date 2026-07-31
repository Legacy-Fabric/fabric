package net.legacyfabric.fabric.api.effect;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

import net.minecraft.potion.Potion;

import net.legacyfabric.fabric.impl.effect.versioned.PotionRegistryImpl;

public final class PotionRegistry {
	public static final ResourceKey<Registry<Potion>> KEY = PotionRegistryImpl.KEY;
	public static final DefaultedRegistry<Potion> REGISTRY = PotionRegistryImpl.REGISTRY;

	public static int getId(Potion potion) {
		return PotionRegistryImpl.getId(potion);
	}

	public static NamespacedIdentifier getIdentifier(Potion potion) {
		return PotionRegistryImpl.getIdentifier(potion);
	}

	public static ResourceKey<Potion> getKey(Potion potion) {
		return PotionRegistryImpl.getKey(potion);
	}

	public static Potion getPotion(int id) {
		return PotionRegistryImpl.getPotion(id);
	}

	public static Potion getPotion(NamespacedIdentifier identifier) {
		return PotionRegistryImpl.getPotion(identifier);
	}

	public static Potion getPotion(ResourceKey<Potion> key) {
		return PotionRegistryImpl.getPotion(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return PotionRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<Potion>> keySet() {
		return PotionRegistryImpl.keySet();
	}

	public static <T extends Potion> T register(NamespacedIdentifier identifier, T potion) {
		return PotionRegistryImpl.register(identifier, potion);
	}

	public static <T extends Potion> T register(ResourceKey<Potion> key, T potion) {
		return PotionRegistryImpl.register(key, potion);
	}
}
