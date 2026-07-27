/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

/**
 * A utility class helping to manage registries and their entries.
 * @deprecated Use dedicated content registry events from OSL and LFAPI
 */
@Deprecated
public class RegistryHelper {
	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 *
	 * @deprecated Use {@link #register(FabricRegistry, NamespacedIdentifier, T)} instead.
	 */
	@Deprecated
	public static <T> void register(FabricRegistry<T> registry, Identifier identifier, T value) {
		RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registry), identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 */
	@Deprecated
	public static <T> void register(FabricRegistry<T> registry, NamespacedIdentifier identifier, T value) {
		RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registry), identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 *
	 * @deprecated Use {@link #register(NamespacedIdentifier, NamespacedIdentifier, T)} instead.
	 */
	@Deprecated
	public static <T> void register(Identifier registryId, Identifier identifier, T value) {
		RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registryId), identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param value The entry
	 * @param <T> The entry type
	 */
	@Deprecated
	public static <T> void register(NamespacedIdentifier registryId, NamespacedIdentifier identifier, T value) {
		RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registryId), identifier, value);
	}

	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 *
	 * @deprecated Use {@link #register(FabricRegistry, NamespacedIdentifier, Function)} instead.
	 */
	@Deprecated
	public static <T> T register(FabricRegistry<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registry), identifier, valueConstructor);
	}

	/**
	 * Register an entry to a registry.
	 * @param registry The registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 */
	@Deprecated
	public static <T> T register(FabricRegistry<T> registry, NamespacedIdentifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(RegistryHelperImplementation.getRegistryCompat(registry), identifier, valueConstructor);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 *
	 * @deprecated Use {@link #register(NamespacedIdentifier, NamespacedIdentifier, Function)} instead.
	 */
	@Deprecated
	public static <T> T register(Identifier registryId, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(RegistryHelperImplementation.<T>getRegistryCompat(registryId), identifier, valueConstructor);
	}

	/**
	 * Register an entry to a registry.
	 * @param registryId The identifier of the registry to register to
	 * @param identifier The entry's identifier
	 * @param valueConstructor The function to create the entry from its assigned numerical id
	 * @param <T> The entry type
	 * @return The entry
	 */
	@Deprecated
	public static <T> T register(NamespacedIdentifier registryId, NamespacedIdentifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(RegistryHelperImplementation.<T>getRegistryCompat(registryId), identifier, valueConstructor);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registry The registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 *
	 * @deprecated Use {@link #getValue(FabricRegistry, NamespacedIdentifier)} instead.
	 */
	@Deprecated
	public static <T> T getValue(FabricRegistry<T> registry, Identifier identifier) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registry);
		return oslRegistry.get(identifier);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registry The registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 */
	@Deprecated
	public static <T> T getValue(FabricRegistry<T> registry, NamespacedIdentifier identifier) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registry);
		return oslRegistry.get(identifier);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 *
	 * @deprecated Use {@link #getValue(NamespacedIdentifier, NamespacedIdentifier)} instead.
	 */
	@Deprecated
	public static <T> T getValue(Identifier registryId, Identifier identifier) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);
		return oslRegistry.get(identifier);
	}

	/**
	 * Get the entry associated to the identifier in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param identifier The identifier of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that identifier
	 */
	@Deprecated
	public static <T> T getValue(NamespacedIdentifier registryId, NamespacedIdentifier identifier) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);
		return oslRegistry.get(identifier);
	}

	/**
	 * Get the identifier associated to the entry in the specified registry.
	 * @param registry The registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The identifier associated to that entry
	 */
	@Deprecated
	public static <T> Identifier getId(FabricRegistry<T> registry, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registry);
		return Identifier.fromNamespaceIdentifier(oslRegistry.getIdentifier(object));
	}

	/**
	 * Get the identifier associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The identifier associated to that entry
	 *
	 * @deprecated Use {@link #getId(NamespacedIdentifier, T)} instead.
	 */
	@Deprecated
	public static <T> Identifier getId(Identifier registryId, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);
		return Identifier.fromNamespaceIdentifier(oslRegistry.getIdentifier(object));
	}

	/**
	 * Get the identifier associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The identifier associated to that entry
	 */
	@Deprecated
	public static <T> NamespacedIdentifier getId(NamespacedIdentifier registryId, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);
		return oslRegistry.getIdentifier(object);
	}

	/**
	 * Get the numerical id associated to the entry in the specified registry.
	 * @param registry The registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The numerical id associated to that entry
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	@Deprecated
	public static <T> int getRawId(FabricRegistry<T> registry, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registry);

		return oslRegistry.getId(object);
	}

	/**
	 * Get the numerical id associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The numerical associated to that entry
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 *
	 * @deprecated Use {@link #getRawId(NamespacedIdentifier, T)} instead.
	 */
	@Deprecated
	public static <T> int getRawId(Identifier registryId, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);

		return oslRegistry.getId(object);
	}

	/**
	 * Get the numerical id associated to the entry in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param object The entry
	 * @param <T> The entry type
	 * @return The numerical associated to that entry
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	@Deprecated
	public static <T> int getRawId(NamespacedIdentifier registryId, T object) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);

		return oslRegistry.getId(object);
	}

	/**
	 * Get the entry associated to the numerical id in the specified registry.
	 * @param registry The registry to look into
	 * @param rawId The numerical id of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that numerical id
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	@Deprecated
	public static <T> T getValue(FabricRegistry<T> registry, int rawId) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registry);

		return oslRegistry.get(rawId);
	}

	/**
	 * Get the entry associated to the numerical id in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param rawId The numerical id of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that numerical id
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 *
	 * @deprecated Use {@link #getValue(NamespacedIdentifier, int)} instead.
	 */
	@Deprecated
	public static <T> T getValue(Identifier registryId, int rawId) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);

		return oslRegistry.get(rawId);
	}

	/**
	 * Get the entry associated to the numerical id in the specified registry.
	 * @param registryId The identifier of the registry to look into
	 * @param rawId The numerical id of the entry to look for
	 * @param <T> The entry type
	 * @return The entry associated to that numerical id
	 * @throws IllegalArgumentException When the registry doesn't support numerical ids.
	 */
	@Deprecated
	public static <T> T getValue(NamespacedIdentifier registryId, int rawId) {
		Registry<T> oslRegistry = RegistryHelperImplementation.getRegistryCompat(registryId);

		return oslRegistry.get(rawId);
	}
}
