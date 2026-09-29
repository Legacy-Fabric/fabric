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

package net.legacyfabric.fabric.api.entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.entity.EntityType;

import net.legacyfabric.fabric.api.util.Identifier;

@Deprecated
public interface EntityHelper {
	/**
	 * @deprecated Use {@link EntityTypeRegistry#registerSpawnEgg(EntityType, int, int)}
	 */
	@Deprecated
	static void registerSpawnEgg(Identifier identifier, int color0, int color1) {
		registerSpawnEgg((NamespacedIdentifier) identifier, color0, color1);
	}

	/**
	 * @deprecated Use {@link EntityTypeRegistry#registerSpawnEgg(EntityType, int, int)}
	 */
	@Deprecated
	static void registerSpawnEgg(NamespacedIdentifier identifier, int color0, int color1) {
		EntityTypeRegistry.registerSpawnEgg(EntityTypeRegistry.getEntityType(identifier), color0, color1);
	}
}
