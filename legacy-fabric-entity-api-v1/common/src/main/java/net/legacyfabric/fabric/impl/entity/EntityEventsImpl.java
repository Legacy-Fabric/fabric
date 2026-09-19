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

package net.legacyfabric.fabric.impl.entity;

import java.util.Map;
import java.util.function.Function;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

import net.minecraft.entity.Entity;

public class EntityEventsImpl implements ModInitializer {
	@Override
	public void init() {
		EntityEvents.REGISTER_ENTITY_TYPES.register(() -> net.legacyfabric.fabric.api.entity.EntityEvents.REGISTER_ENTITIES.invoker().run());
	}

	public static Object fixOldRegistryName(String entityId, Function<String, Object> getter, Map<Class<? extends Entity>, String> TYPE_TO_KEY) {
		Object o = getter.apply(entityId);

		if (o != null) return o;

		if (entityId.contains(".")) {
			entityId = entityId.replace(".", ":");
		}

		NamespacedIdentifier identifier = NamespacedIdentifiers.parse(entityId);
		Class<? extends Entity> clazz = EntityTypeRegistry.getEntityType(identifier);

		if (clazz != null) {
			return getter.apply(TYPE_TO_KEY.get(clazz));
		}

		return null;
	}
}
