/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.mixin.content.registries.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.content.registry.v1.RarityProvider;

@Environment(EnvType.CLIENT)
@Mixin(CreativeInventoryScreen.class)
public class MixinCreativeInventoryScreen {
	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER, remap = false), method = "renderTooltip", locals = LocalCapture.CAPTURE_FAILSOFT)
	public void renderTooltipHook(ItemStack stack, int x, int y, CallbackInfo ci, List<String> list, int i) {
		if (stack.getItem() instanceof RarityProvider) {
			list.set(i, ((RarityProvider) stack.getItem()).getFormatting(stack) + list.get(i));
		}
	}
}
