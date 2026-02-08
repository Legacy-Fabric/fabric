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

package net.legacyfabric.fabric.test.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.OverworldGeneratorOptions;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;

@Mixin(OverworldGeneratorOptions.Serializer.class)
public class CustomizedWorldPropertiesSerializerMixin {
	@ModifyConstant(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/world/gen/chunk/OverworldGeneratorOptions$Builder;",
			constant = @Constant(intValue = 38))
	private int fixBiomeSelector(int max) {
		SyncedFabricRegistry<Biome> registry = (SyncedFabricRegistry<Biome>) (Object) RegistryHelper.getRegistry(RegistryIds.BIOMES);
		return registry.stream().mapToInt(b -> registry.fabric$getRawId(b)).max().orElse(max) - 1;
	}
}
