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

package net.legacyfabric.fabric.api.biome;

import net.ornithemc.osl.registries.api.registry.Registry;

import net.minecraft.world.biome.Biome;

@Deprecated
public interface BiomeExtension extends net.ornithemc.osl.biomes.api.biome.BiomeExtension {
	/**
	 * @deprecated Use {@link net.ornithemc.osl.biomes.api.biome.BiomeExtension#REGISTRY}
	 */
	@Deprecated
	Registry<Biome> BIOME_REGISTRY = REGISTRY;
	/**
	 * @deprecated Use {@link net.ornithemc.osl.biomes.api.biome.BiomeExtension#AUTO_ASSIGN_ID}
	 */
	@Deprecated
	int REGISTRY_AUTO_ASSIGN_ID = AUTO_ASSIGN_ID;
}
