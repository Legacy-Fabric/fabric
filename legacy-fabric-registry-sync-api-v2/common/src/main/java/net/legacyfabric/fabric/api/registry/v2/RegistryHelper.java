/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.api.registry.v2;

import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.RegistryEntryCreator;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

/**
 * A utility class helping to manage registries and their entries.
 */
public class RegistryHelper {
	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 */
	public static <T> void register(FabricRegistry<T> registry, Identifier identifier, T value) {
		RegistryHelperImplementation.register(registry, identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 */
	public static <T> void register(Identifier registryId, Identifier identifier, T value) {
		register(RegistryHelperImplementation.getRegistry(registryId), identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 */
	public static <T> T register(FabricRegistry<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(registry, identifier, valueConstructor);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 */
	public static <T> T register(Identifier registryId, Identifier identifier, Function<Integer, T> valueConstructor) {
		return register(RegistryHelperImplementation.<T>getRegistry(registryId), identifier, valueConstructor);
	}

	/**
	 * Register a registry.
	 * @param identifier The registry's identifier
	 * @param registry The registry
	 */
	public static void addRegistry(Identifier identifier, FabricRegistry<?> registry) {
		RegistryHelperImplementation.registerRegistry(identifier, registry);
	}

	/**
	 * Get a registry by its identifier.
	 * @param identifier The registry identifier
	 * @param <T> The registry's entry type
	 * @return The associated registry
	 */
	public static <T> FabricRegistry<T> getRegistry(Identifier identifier) {
		return RegistryHelperImplementation.getRegistry(identifier);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registry The registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 */
	public static <T> T getValue(FabricRegistry<T> registry, Identifier identifier) {
		return registry.fabric$getValue(identifier);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 */
	public static <T> T getValue(Identifier registryId, Identifier identifier) {
		return RegistryHelperImplementation.<T>getRegistry(registryId)
				.fabric$getValue(identifier);
	}

	/**
	 * Get the identifier associated to the entry in the specified registry.
	 * @param registry The registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The identifier associated to that entry
	 */
	public static <T> Identifier getId(FabricRegistry<T> registry, T object) {
		return registry.fabric$getId(object);
	}

	/**
	 * Get the identifier associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The identifier associated to that entry
	 */
	public static <T> Identifier getId(Identifier registryId, T object) {
		return getId(RegistryHelperImplementation.getRegistry(registryId), object);
	}

	/**
	 * Get the numerical id associated to the entry in the specified registry.
	 * @param registry The registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The numerical id associated to that entry
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	public static <T> int getRawId(FabricRegistry<T> registry, T object) {
		if (!(registry instanceof SyncedFabricRegistry)) {
			throw new IllegalArgumentException("Cannot get raw id of " + object + " of non synced registry " + registry.fabric$getId());
		}

		return ((SyncedFabricRegistry<T>) registry).fabric$getRawId(object);
	}

	/**
	 * Get the numerical id associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The numerical associated to that entry
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	public static <T> int getRawId(Identifier registryId, T object) {
		return getRawId(RegistryHelperImplementation.getRegistry(registryId), object);
	}

	/**
	 * Get the entry associated to the numerical id in the specified registry.
	 * @param registry The registry to look into
	 * @param rawId The numerical id of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that numerical id
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	public static <T> T getValue(FabricRegistry<T> registry, int rawId) {
		if (!(registry instanceof SyncedFabricRegistry)) {
			throw new IllegalArgumentException("Cannot get value of id " + rawId + " of non synced registry " + registry.fabric$getId());
		}

		return ((SyncedFabricRegistry<T>) registry).fabric$getValue(rawId);
	}

	/**
	 * Get the entry associated to the numerical id in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param rawId The numerical id of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that numerical id
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	public static <T> T getValue(Identifier registryId, int rawId) {
		return getValue(RegistryHelperImplementation.getRegistry(registryId), rawId);
	}

	@ApiStatus.Experimental
	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, Function<Integer, T> valueConstructor, int offset) {
		return RegistryHelperImplementation.createEntryCreator(identifier, valueConstructor, offset);
	}

	@ApiStatus.Experimental
	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, T value, int offset) {
		return createEntryCreator(identifier, (id) -> value, offset);
	}

	@ApiStatus.Experimental
	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, Function<Integer, T> valueConstructor) {
		return createEntryCreator(identifier, valueConstructor, 0);
	}

	@ApiStatus.Experimental
	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, T value) {
		return createEntryCreator(identifier, value, 0);
	}

	// TODO Finish remapping part of it
	//	public static <T> List<RegistryEntry<T>> registerMultiple(Registry<T> registry, RegistryEntryCreator<T>... creators) {
	//		return RegistryHelperImplementation.register(registry, creators);
	//	}
	//
	//	public static <T> List<RegistryEntry<T>> registerMultiple(Identifier registryId, RegistryEntryCreator<T>... creators) {
	//		return registerMultiple(RegistryHelperImplementation.getRegistry(registryId), creators);
	//	}
}
