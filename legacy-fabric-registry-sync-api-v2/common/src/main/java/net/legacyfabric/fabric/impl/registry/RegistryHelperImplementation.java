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

package net.legacyfabric.fabric.impl.registry;

import java.util.HashMap;
import java.util.Map;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

public class RegistryHelperImplementation {
	private static final Map<String, String> BACKWARD_COMPATIBILITY = new HashMap<>();
	static {
		BACKWARD_COMPATIBILITY.put("Items", RegistryIds.ITEMS.toString());
		BACKWARD_COMPATIBILITY.put("Blocks", RegistryIds.BLOCKS.toString());
		BACKWARD_COMPATIBILITY.put("Biomes", RegistryIds.BIOMES.toString());
		BACKWARD_COMPATIBILITY.put("BlockEntityTypes", RegistryIds.BLOCK_ENTITY_TYPES.toString());
		BACKWARD_COMPATIBILITY.put("Enchantments", RegistryIds.ENCHANTMENTS.toString());
		BACKWARD_COMPATIBILITY.put("EntityTypes", RegistryIds.ENTITY_TYPES.toString());
		BACKWARD_COMPATIBILITY.put("StatusEffects", RegistryIds.STATUS_EFFECTS.toString());
	}

	public static String convertRegistryId(String key) {
		if (BACKWARD_COMPATIBILITY.containsKey(key)) {
			key = BACKWARD_COMPATIBILITY.get(key);
		}

		return key.substring(0, key.length() - 1);
	}
}
