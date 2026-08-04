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

package net.legacyfabric.fabric.mixin.biome;

import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;
import net.legacyfabric.fabric.impl.biome.versioned.BiomeRegistryImpl;

import net.minecraft.util.Id2ObjectBiMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.Id2ObjectBiMapMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.world.biome.Biome;

@Mixin(Biome.class)
public class BiomeMixin {
	@Shadow
	@Final
	public static IdRegistry<Identifier, Biome> REGISTRY;

	@Shadow
	@Final
	public static Id2ObjectBiMap<Biome> MUTATED_BIOMES;

	@Inject(method = "init", at = @At("HEAD"))
	private static void lf$unlockRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.unlock();
	}

	@Inject(method = "init()V", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();

		SyncedRegistries.registerMapper(VanillaRegistryKeys.BIOME, NamespacedIdentifiers.from("biome/mutated_biomes"), Id2ObjectBiMapMapper.of(MUTATED_BIOMES));
	}
}
