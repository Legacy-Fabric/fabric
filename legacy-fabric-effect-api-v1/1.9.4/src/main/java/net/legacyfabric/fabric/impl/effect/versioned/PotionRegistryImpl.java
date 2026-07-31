package net.legacyfabric.fabric.impl.effect.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.potion.Potion;

import net.legacyfabric.fabric.api.effect.PotionEvents;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

public class PotionRegistryImpl {
	public static final ResourceKey<Registry<Potion>> KEY = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("potion");
	public static final DefaultedRegistry<Potion> REGISTRY = VanillaRegistries.registerDefaulted(KEY, Potion.REGISTRY);

	private static boolean locked = true;

	public static int getId(Potion potion) {
		return REGISTRY.getId(potion);
	}

	public static NamespacedIdentifier getIdentifier(Potion potion) {
		return REGISTRY.getIdentifier(potion);
	}

	public static ResourceKey<Potion> getKey(Potion potion) {
		return REGISTRY.getKey(potion);
	}

	public static Potion getPotion(int id) {
		return REGISTRY.get(id);
	}

	public static Potion getPotion(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Potion getPotion(ResourceKey<Potion> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Potion>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Potion> T register(NamespacedIdentifier identifier, T potion) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, potion);
		}
	}

	public static <T extends Potion> T register(ResourceKey<Potion> identifier, T potion) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, potion);
		}
	}

	public static void init() {
		SyncedRegistries.register(KEY);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerPotions() {
		PotionEvents.REGISTER_POTIONS.invoker().run();
		RegistryHelperImplementation.registerCompatId(RegistryIds.POTIONS, REGISTRY);
		RegistryHelperImplementation.registerCompatRegistry((FabricRegistry<Potion>) Potion.REGISTRY, REGISTRY);
	}
}
