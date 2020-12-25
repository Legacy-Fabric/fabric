/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.legacyfabric.fabric.api.fluid.volume.v1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidStorage;
import net.legacyfabric.fabric.api.fluid.volume.v1.store.FluidStorageHandler;

public class FluidHandlers {
	private static final HashMap<Predicate<Object>, Function<Object, FluidStorage>> HOLDERS = new LinkedHashMap<>();

	public static <T> void register(Class<T> clazz, Function<Object, FluidStorage> holderFunction) {
		register(object -> object.getClass() == clazz, holderFunction);
	}

	public static void register(Predicate<Object> supports, Function<Object, FluidStorage> holderFunction) {
		HOLDERS.put(supports, holderFunction);
	}

	public static FluidStorageHandler get(Object object, FluidType fluidType) {
		return getOptional(object, fluidType).orElseThrow(() -> new RuntimeException(String.format("object type (%s) not supported", object.getClass().getName())));
	}

	public static Optional<FluidStorageHandler> getOptional(Object object, FluidType fluidType) {
		if (object == null) {
			return Optional.empty();
		}

		for (Map.Entry<Predicate<Object>, Function<Object, FluidStorage>> holder : HOLDERS.entrySet()) {
			if (holder.getKey().test(object)) {
				return Optional.of(new FluidStorageHandler(holder.getValue().apply(object), fluidType));
			}
		}

		return Optional.empty();
	}

	public static boolean isValid(Object object) {
		for (Predicate<Object> predicate : HOLDERS.keySet()) {
			if (predicate.test(object)) {
				return true;
			}
		}

		return false;
	}

	static {
		register(object -> object instanceof FluidStorage, object -> (FluidStorage) object);
	}
}
