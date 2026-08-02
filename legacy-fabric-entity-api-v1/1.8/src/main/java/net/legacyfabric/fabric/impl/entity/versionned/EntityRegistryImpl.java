package net.legacyfabric.fabric.impl.entity.versionned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.entity.Entity;
import net.minecraft.util.registry.IdRegistry;

import net.legacyfabric.fabric.api.entity.EntityEvents;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;
import net.legacyfabric.fabric.impl.entity.VanillaEntityTypes;
import net.legacyfabric.fabric.mixin.entity.EntitiesAccessor;

public class EntityRegistryImpl {
	public static final Registry<Class<? extends Entity>> REGISTRY = VanillaRegistries.registerSimple(VanillaRegistryKeys.ENTITY_TYPE, new IdRegistry());

	private static boolean locked = true;

	public static int getId(Class<? extends Entity> type) {
		return REGISTRY.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(Class<? extends Entity> type) {
		return REGISTRY.getIdentifier(type);
	}

	public static ResourceKey<Class<? extends Entity>> getKey(Class<? extends Entity> type) {
		return REGISTRY.getKey(type);
	}

	public static Class<? extends Entity> getEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Class<? extends Entity> getEntityType(ResourceKey<Class<? extends Entity>> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Class<? extends Entity>>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Entity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			Class<T> clazz = Registry.register(REGISTRY, identifier, type);
			registerCommon(identifier, clazz);
			return clazz;
		}
	}

	public static <T extends Entity> Class<T> register(ResourceKey<Class<? extends Entity>> identifier, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			Class<T> clazz = Registry.register(REGISTRY, identifier, type);
			registerCommon(identifier.identifier(), clazz);
			return clazz;
		}
	}

	private static <T extends Entity> void registerCommon(NamespacedIdentifier identifier, Class<T> type) {
		int id = getId(type);
		String name = Util.makeTranslationKey(identifier);

		EntitiesAccessor.getKey2Type().put(name, type);
		EntitiesAccessor.getType2Key().put(type, name);
		EntitiesAccessor.getId2Type().put(id, type);
		EntitiesAccessor.getType2Id().put(type, id);
		EntitiesAccessor.getKey2Id().put(name, id);
	}

	public static void init() {
		SyncedRegistries.register(VanillaRegistryKeys.ENTITY_TYPE);
		SyncedRegistries.registerFixer(VanillaRegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.from("spawn_egg_data/id"), new SpawnEggDataFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEffects() {
		VanillaEntityTypes.init(EntitiesAccessor.getId2Type(), REGISTRY);
		EntityEvents.REGISTER_ENTITIES.invoker().run();
	}
}
