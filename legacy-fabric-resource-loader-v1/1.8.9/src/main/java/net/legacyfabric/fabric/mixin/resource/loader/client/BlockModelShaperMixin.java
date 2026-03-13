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

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockModelShaper;
import net.minecraft.client.resource.model.BlockModelProvider;

import net.legacyfabric.fabric.impl.resource.loader.BlockStateVariantRegistryImpl;

@Mixin(BlockModelShaper.class)
public abstract class BlockModelShaperMixin {
	@Shadow
	public abstract void register(Block block, BlockModelProvider provider);

	@Shadow
	public abstract void register(Block... blocks);

	@Inject(method = "init", at = @At("TAIL"))
	private void registerModBlockStateVariants(CallbackInfo ci) {
		for (Map.Entry<Block, BlockModelProvider> entry : BlockStateVariantRegistryImpl.BLOCK_MODEL_PROVIDERS.entrySet()) {
			this.register(entry.getKey(), entry.getValue());
		}

		this.register(BlockStateVariantRegistryImpl.BLOCKS.toArray(new Block[0]));
	}
}
