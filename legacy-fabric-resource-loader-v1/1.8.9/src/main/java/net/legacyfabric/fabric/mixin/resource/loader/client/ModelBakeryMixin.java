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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.resource.model.ModelBakery;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.impl.resource.loader.ItemModelRegistryImpl;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
	@Shadow
	private Map<Item, List<String>> itemVariants;

	@Inject(method = "registerItemVariants", at = @At("TAIL"))
	private void lfapi$registerAdditionalVariants(CallbackInfo ci) {
		for (ItemModelRegistryImpl.ModelTriad<Item> triad : ItemModelRegistryImpl.ITEMS_WITH_META) {
			if (triad.getObject().hasCustomData() && triad.getModel() != null) {
				this.itemVariants.computeIfAbsent(triad.getObject(), key -> new ArrayList<>())
						.add(triad.getModelPath());
			}
		}

		for (ItemModelRegistryImpl.ModelTriad<Block> triad : ItemModelRegistryImpl.BLOCKS_WITH_META) {
			Item item = Item.byBlock(triad.getObject());
			if (item != null && item.hasCustomData() && triad.getModel() != null) {
				this.itemVariants.computeIfAbsent(item, key -> new ArrayList<>())
						.add(triad.getModelPath());
			}
		}
	}
}
