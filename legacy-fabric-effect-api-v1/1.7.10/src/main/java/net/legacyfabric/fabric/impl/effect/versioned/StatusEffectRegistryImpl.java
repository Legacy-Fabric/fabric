package net.legacyfabric.fabric.impl.effect.versioned;

import java.util.Set;

import net.legacyfabric.fabric.api.effect.StatusEffectExtension;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.util.registry.IdRegistry;

import net.legacyfabric.fabric.api.effect.StatusEffectEvents;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

public class StatusEffectRegistryImpl {
	public static final Registry<StatusEffect> REGISTRY = VanillaRegistries.registerSimple(VanillaRegistryKeys.STATUS_EFFECT, new IdRegistry<>());

	private static boolean locked = true;

	public static int getId(StatusEffect effect) {
		return REGISTRY.getId(effect);
	}

	public static NamespacedIdentifier getIdentifier(StatusEffect effect) {
		return REGISTRY.getIdentifier(effect);
	}

	public static ResourceKey<StatusEffect> getKey(StatusEffect effect) {
		return REGISTRY.getKey(effect);
	}

	public static StatusEffect getEffect(int id) {
		return REGISTRY.get(id);
	}

	public static StatusEffect getEffect(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static StatusEffect getEffect(ResourceKey<StatusEffect> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<StatusEffect>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, effect);
		}
	}

	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, effect);
		}
	}

	@Deprecated
	public static <T extends StatusEffect> T register(int id, NamespacedIdentifier key, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (effect.id != id) {
				throw new IllegalArgumentException("ID " + id + " does not match status effect ID " + effect.id + " for " + key);
			}

			return Registry.register(REGISTRY, id, key, effect);
		}
	}

	public static void init() {
		SyncedRegistries.register(VanillaRegistryKeys.STATUS_EFFECT);
		SyncedRegistries.registerFixer(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("status_effect/id"), new StatusEffectIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEffects() {
		VanillaStatusEffects.init();
		StatusEffectEvents.REGISTER_EFFECTS.invoker().run();
		RegistryHelperImplementation.registerCompatId(RegistryIds.STATUS_EFFECTS, REGISTRY);
		RegistryHelperImplementation.registerAutoAssign(REGISTRY, StatusEffectExtension.REGISTRY_AUTO_ASSIGN_ID);
	}
}
