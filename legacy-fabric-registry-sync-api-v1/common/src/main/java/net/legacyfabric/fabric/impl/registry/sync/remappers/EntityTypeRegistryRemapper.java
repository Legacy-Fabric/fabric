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

package net.legacyfabric.fabric.impl.registry.sync.remappers;

import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;

public class EntityTypeRegistryRemapper extends RegistryRemapper<Class<? extends Entity>> {
	public EntityTypeRegistryRemapper() {
		super(RegistryHelperImpl.registriesGetter.getEntityTypeRegistry(), RegistryIds.ENTITY_TYPES, "EntityType", "EntityTypes");
	}

	@Override
	public int getMinId() {
		return 210;
	}
}
