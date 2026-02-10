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

package net.legacyfabric.fabric.impl.entity.versionned;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities__SpawnEggData;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.util.Identifier;

public class EntityHelperImpl {
	public static void registerSpawnEgg(Identifier identifier, int color0, int color1) {
		int mcId = RegistryHelper.getRawId(RegistryIds.ENTITY_TYPES,
				RegistryHelper.getValue(RegistryIds.ENTITY_TYPES, identifier));
		Entities.SPAWN_EGG_DATA.put(mcId, new Entities__SpawnEggData(mcId, color0, color1));
	}
}
