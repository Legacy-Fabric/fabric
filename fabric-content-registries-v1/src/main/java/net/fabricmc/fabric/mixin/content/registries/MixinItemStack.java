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

package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.content.registry.v1.RarityProvider;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	@Shadow
	public abstract Item getItem();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Style;setColor(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", shift = At.Shift.AFTER), method = "toHoverableText", locals = LocalCapture.CAPTURE_FAILSOFT)
	public void toHoverableTextHook(CallbackInfoReturnable<Text> cir, LiteralText literalText, Text text, CompoundTag compoundTag) {
		if (this.getItem() instanceof RarityProvider) {
			text.getStyle().setColor(((RarityProvider) this.getItem()).getFormatting());
		}
	}
}
