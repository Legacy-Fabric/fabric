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

package net.legacyfabric.fabric.test.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.CustomizeWorldScreen;
import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;

@Mixin(CustomizeWorldScreen.class)
public class CustomizeWorldScreenMixin {
	@ModifyArg(method = "initPages",
			at = @At(value = "INVOKE", ordinal = 4, target = "Lnet/minecraft/client/gui/widget/PagedEntryListWidget$LabelSupplierEntry;<init>(ILjava/lang/String;ZLnet/minecraft/client/gui/widget/SliderWidget$LabelSupplier;FFF)V"),
			index = 5
	)
	private float allowSelectingAllBiomesInSelector(float max) {
		SyncedFabricRegistry<Biome> registry = (SyncedFabricRegistry<Biome>) (Object) RegistryHelper.getRegistry(RegistryIds.BIOMES);
		return registry.stream().mapToInt(b -> registry.fabric$getRawId(b)).max().orElse((int) max) - 1;
	}
}
