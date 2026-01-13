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

package net.legacyfabric.fabric.mixin.block.versioned;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(Block.class)
public class BlockMixin {
	@Shadow
	@Final
	public static SimpleRegistry REGISTRY;

	@Inject(method = "setup", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		RegistryHelper.addRegistry(RegistryIds.BLOCKS, REGISTRY);
	}

	@Inject(method = "getBlockFromItem", at = @At("HEAD"), cancellable = true)
	private static void fixBlockFromItem(Item item, CallbackInfoReturnable<Block> cir) {
		if (item instanceof BlockItem) {
			cir.setReturnValue(((BlockItemAccessor) item).getBlock());
			return;
		}

		Identifier identifier = RegistryHelper.getId(Item.REGISTRY, item);

		if (identifier != null) {
			Block blockFromId = RegistryHelper.<Block>getValue(REGISTRY, identifier);

			if (blockFromId != null) {
				cir.setReturnValue(blockFromId);
				return;
			}
		}

		cir.setReturnValue(null);
	}
}
