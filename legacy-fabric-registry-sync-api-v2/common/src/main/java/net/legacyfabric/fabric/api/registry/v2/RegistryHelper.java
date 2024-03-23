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

import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

public class RegistryHelper {
	public static <T> void register(RegistryHolder<T> registry, Identifier identifier, T value) {
		RegistryHelperImplementation.register(registry, identifier, value);
	}

	public static <T> void register(Identifier registryId, Identifier identifier, T value) {
		register(RegistryHelperImplementation.getRegistry(registryId), identifier, value);
	}

	public static <T> T register(RegistryHolder<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(registry, identifier, valueConstructor);
	}

	public static <T> T register(Identifier registryId, Identifier identifier, Function<Integer, T> valueConstructor) {
		return register(RegistryHelperImplementation.<T>getRegistry(registryId), identifier, valueConstructor);
	}

	public static void addRegistry(Identifier identifier, RegistryHolder<?> registryHolder) {
		RegistryHelperImplementation.registerRegistry(identifier, registryHolder);
	}

	public static <T> RegistryHolder<T> getRegistry(Identifier identifier) {
		return RegistryHelperImplementation.getRegistry(identifier);
	}

	public static <T> T getValue(Identifier registryId, Identifier identifier) {
		return RegistryHelperImplementation.<T>getRegistry(registryId)
				.fabric$getValue(identifier);
	}

	public static <T> T getValue(RegistryHolder<T> registry, Identifier identifier) {
		return registry.fabric$getValue(identifier);
	}
}
