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

package net.legacyfabric.fabric.mixin.item.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.item.versioned.ItemModelsRemapper;

@Mixin(ItemModels.class)
public class ItemModelsMixin {
	@Inject(method = "putModel", at = @At("RETURN"))
	private void lf$registerModelId(Item item, int metadata, ModelIdentifier id, CallbackInfo ci) {
		Identifier identifier = RegistryHelper.getId(Item.REGISTRY, item);

		if (identifier != null) ItemModelsRemapper.registerModelId(identifier, metadata, id);
	}
}
