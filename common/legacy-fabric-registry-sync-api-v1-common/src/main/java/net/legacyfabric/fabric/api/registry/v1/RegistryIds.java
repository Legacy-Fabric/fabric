/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.api.registry.v1;

import net.legacyfabric.fabric.api.util.Identifier;

public class RegistryIds {
	public static final Identifier REGISTRY_REMAPPER = new Identifier("legacy-fabric-registry-sync-api-v1-common", "registry_remappers");
	public static final Identifier ITEMS = new Identifier("items");
	public static final Identifier BLOCKS = new Identifier("blocks");
	public static final Identifier BLOCK_ENTITY_TYPES = new Identifier("block_entity_types");
	public static final Identifier STATUS_EFFECTS = new Identifier("status_effects");
	public static final Identifier ENCHANTMENTS = new Identifier("enchantments");
	public static final Identifier BIOMES = new Identifier("biomes");
	public static final Identifier ENTITY_TYPES = new Identifier("entity_types");
}
