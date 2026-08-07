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

import java.util.Map;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities__SpawnEggData;

public class SpawnEggDataFixer implements IdFixer {
	@Override
	public void apply() {
		for (Map.Entry<Integer, Entities__SpawnEggData> entry : ((Map<Integer, Entities__SpawnEggData>) Entities.SPAWN_EGG_DATA).entrySet()) {
			int newId = entry.getKey();
			Entities__SpawnEggData data = entry.getValue();

			data.id = newId;
		}
	}
}
