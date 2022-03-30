/*
 * Copyright (c) 2021 Legacy Rewoven
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

package net.legacyfabric.fabric.mixin.base.event;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.IdentifibleBooleanConsumer;

import net.fabricmc.loader.impl.FabricLoaderImpl;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen implements IdentifibleBooleanConsumer {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 0))
	public void brandingMixin(TitleScreen instance, TextRenderer textRenderer, String s, int i, int j, int k) {
		String modifiedVar9 = s +"/Fabric " + FabricLoaderImpl.VERSION + " (" + FabricLoaderImpl.INSTANCE.getAllMods().size() + " mods loaded)";
		instance.drawWithShadow(textRenderer, modifiedVar9, i, j, k);
	}
}
