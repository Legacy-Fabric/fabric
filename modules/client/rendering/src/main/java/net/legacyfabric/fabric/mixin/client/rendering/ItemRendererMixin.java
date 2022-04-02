/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.mixin.client.rendering;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.item.ItemRenderer;
//TODO: Item Renderer

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	/*@Inject(method = "method_3982", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityItemStackRenderHelper;method_3349(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
	public void onRender(ItemStack stack, BakedModel bakedModel, CallbackInfo ci) {
		BuiltinItemRendererRegistry.DynamicItemRenderer renderer = BuiltinItemRendererRegistryImpl.getRenderer(stack.getItem());

		if (renderer != null) {
			renderer.render(stack, bakedModel.getTransformation());
		}
	}*/
}
