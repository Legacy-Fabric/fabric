/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.RegistryEntryCreator;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

public class RegistryHelper {
	public static <T> void register(Registry<T> registry, Identifier identifier, T value) {
		RegistryHelperImplementation.register(registry, identifier, value);
	}

	public static <T> void register(Identifier registryId, Identifier identifier, T value) {
		register(RegistryHelperImplementation.getRegistry(registryId), identifier, value);
	}

	public static <T> T register(Registry<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(registry, identifier, valueConstructor);
	}

	public static <T> T register(Identifier registryId, Identifier identifier, Function<Integer, T> valueConstructor) {
		return register(RegistryHelperImplementation.<T>getRegistry(registryId), identifier, valueConstructor);
	}

	public static void addRegistry(Identifier identifier, Registry<?> registry) {
		RegistryHelperImplementation.registerRegistry(identifier, registry);
	}

	public static <T> Registry<T> getRegistry(Identifier identifier) {
		return RegistryHelperImplementation.getRegistry(identifier);
	}

	public static <T> T getValue(Identifier registryId, Identifier identifier) {
		return RegistryHelperImplementation.<T>getRegistry(registryId)
				.fabric$getValue(identifier);
	}

	public static <T> T getValue(Registry<T> registry, Identifier identifier) {
		return registry.fabric$getValue(identifier);
	}

	public static <T> Identifier getId(Registry<T> registry, T object) {
		return registry.fabric$getId(object);
	}

	public static <T> Identifier getId(Identifier registryId, T object) {
		return getId(RegistryHelperImplementation.getRegistry(registryId), object);
	}

	public static <T> int getRawId(Registry<T> registry, T object) {
		if (!(registry instanceof SyncedRegistry)) {
			throw new IllegalArgumentException("Cannot get raw id of " + object + " of non synced registry " + registry.fabric$getId());
		}

		return ((SyncedRegistry<T>) registry).fabric$getRawId(object);
	}

	public static <T> int getRawId(Identifier registryId, T object) {
		return getRawId(RegistryHelperImplementation.getRegistry(registryId), object);
	}

	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, Function<Integer, T> valueConstructor, int offset) {
		return RegistryHelperImplementation.createEntryCreator(identifier, valueConstructor, offset);
	}

	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, T value, int offset) {
		return createEntryCreator(identifier, (id) -> value, offset);
	}

	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, Function<Integer, T> valueConstructor) {
		return createEntryCreator(identifier, valueConstructor, 0);
	}

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
