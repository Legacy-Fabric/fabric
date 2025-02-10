/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.block.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "fromBlock", at = @At("HEAD"), cancellable = true)
	private static void fixItemFromBlock(Block block, CallbackInfoReturnable<Item> cir) {
		Identifier identifier = RegistryHelper.getId(Block.REGISTRY, block);

		if (identifier != null) {
			Item item = RegistryHelper.<Item>getValue(Item.REGISTRY, identifier);

			if (item != null) {
				cir.setReturnValue(item);
				return;
			}
		}

		cir.setReturnValue(null);
	}
}
