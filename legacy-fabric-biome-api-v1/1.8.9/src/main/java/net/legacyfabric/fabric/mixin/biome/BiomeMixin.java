/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.biome.EarlyInitializer;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayFabricRegistryWrapper;

@Mixin(Biome.class)
public class BiomeMixin {
	@Mutable
	@Shadow
	@Final
	private static Biome[] BY_ID;
	@Unique
	private static FabricRegistry<Biome> REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		REGISTRY = new SyncedArrayFabricRegistryWrapper<>(
				RegistryIds.BIOMES,
				BY_ID, EarlyInitializer.getVanillaIds(),
				universal -> universal,
				id -> id,
				ids -> {
					Biome[] array = new Biome[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (Biome enchantment : ids) {
						int id = ids.fabric$getId(enchantment);

						if (id >= array.length - 1) {
							Biome[] newArray = new Biome[id + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = enchantment;
					}

					BY_ID = array;
				}
		);

		RegistryHelper.addRegistry(RegistryIds.BIOMES, REGISTRY);
	}
}
