package net.legacyfabric.fabric.api.entity;

import net.legacyfabric.fabric.impl.entity.versionned.EntityRegistryImpl;

import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

import java.util.Set;

public final class EntityRegistry {
	public static final Registry<Class<? extends Entity>> REGISTRY = EntityRegistryImpl.REGISTRY;

	public static int getId(Class<? extends Entity> type) {
		return EntityRegistryImpl.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(Class<? extends Entity> type) {
		return EntityRegistryImpl.getIdentifier(type);
	}

	public static ResourceKey<Class<? extends Entity>> getKey(Class<? extends Entity> type) {
		return EntityRegistryImpl.getKey(type);
	}

	public static Class<? extends Entity> getEntityType(int id) {
		return EntityRegistryImpl.getEntityType(id);
	}

	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return EntityRegistryImpl.getEntityType(identifier);
	}

	public static Class<? extends Entity> getEntityType(ResourceKey<Class<? extends Entity>> key) {
		return EntityRegistryImpl.getEntityType(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return EntityRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<Class<? extends Entity>>> keySet() {
		return EntityRegistryImpl.keySet();
	}

	public static <T extends Entity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		return EntityRegistryImpl.register(identifier, type);
	}

	public static <T extends Entity> Class<T> register(ResourceKey<Class<? extends Entity>> key, Class<T> type) {
		return EntityRegistryImpl.register(key, type);
	}
}
