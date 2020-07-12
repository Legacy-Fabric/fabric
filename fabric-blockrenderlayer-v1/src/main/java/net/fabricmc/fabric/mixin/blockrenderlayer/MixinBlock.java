/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.mixin.blockrenderlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class MixinBlock {
	@Inject(method = "getRenderLayerType", at = @At("HEAD"), cancellable = true)
	public void setRenderLayer(CallbackInfoReturnable<RenderLayer> cir) {
		if (BlockRenderLayerMapImpl.blockRenderLayerMap.containsKey(this)) {
			cir.setReturnValue(BlockRenderLayerMapImpl.blockRenderLayerMap.get(this));
			cir.cancel();
		}
	}
}
