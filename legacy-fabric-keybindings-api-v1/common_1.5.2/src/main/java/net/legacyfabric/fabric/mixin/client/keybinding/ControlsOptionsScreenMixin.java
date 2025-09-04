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

package net.legacyfabric.fabric.mixin.client.keybinding;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.GameOptions;

import net.legacyfabric.fabric.impl.client.keybinding.ControlsScreenExtensions;
import net.legacyfabric.fabric.impl.client.keybinding.FabricControlsScreenComponents;

@Mixin(ControlsOptionsScreen.class)
public class ControlsOptionsScreenMixin extends Screen implements ControlsScreenExtensions {
	@Shadow
	private GameOptions options;

	private static int fabric_currentPage = 0;

	private int fabric_getPageOffset(int page) {
		switch (page) {
		case 0:
			return 0;
		case 1:
			return FabricControlsScreenComponents.AMOUNT_PER_PAGE;
		default:
			return FabricControlsScreenComponents.AMOUNT_PER_PAGE + ((page - 1) * FabricControlsScreenComponents.AMOUNT_PER_PAGE);
		}
	}

	private int fabric_getOffsetPage(int offset) {
		if (offset < FabricControlsScreenComponents.AMOUNT_PER_PAGE) {
			return 0;
		} else {
			return 1 + ((offset - FabricControlsScreenComponents.AMOUNT_PER_PAGE) / FabricControlsScreenComponents.AMOUNT_PER_PAGE);
		}
	}

	@Override
	public boolean fabric_isButtonVisible(FabricControlsScreenComponents.Type type) {
		return this.options.allKeys.length > FabricControlsScreenComponents.AMOUNT_PER_PAGE;
	}

	@Override
	public void fabric_nextPage() {
		if (fabric_getPageOffset(fabric_currentPage + 1) >= this.options.allKeys.length) {
			return;
		}

		fabric_currentPage++;
		fabric_updateSelection();
	}

	@Override
	public void fabric_previousPage() {
		if (fabric_currentPage == 0) {
			return;
		}

		fabric_currentPage--;
		fabric_updateSelection();
	}

	@Override
	public int fabric_currentPage() {
		return fabric_currentPage;
	}

	@Override
	public boolean fabric_isButtonEnabled(FabricControlsScreenComponents.Type type) {
		if (type == FabricControlsScreenComponents.Type.NEXT) {
			return !(fabric_getPageOffset(fabric_currentPage + 1) >= this.options.allKeys.length);
		}

		if (type == FabricControlsScreenComponents.Type.PREVIOUS) {
			return fabric_currentPage != 0;
		}

		return false;
	}

	@Inject(at = @At("HEAD"), method = "buttonClicked", cancellable = true)
	private void interceptClick(ButtonWidget button, CallbackInfo ci) {
		if (button instanceof FabricControlsScreenComponents.ControlsButtonWidget) {
			((FabricControlsScreenComponents.ControlsButtonWidget) button).click();
			ci.cancel();
		}
	}

	private boolean fabric_isControlVisible(int id) {
		return fabric_currentPage == fabric_getOffsetPage(id);
	}

	private void fabric_updateSelection() {
		for (Object widget : this.buttons) {
			if (widget instanceof OptionButtonWidget) {
				((OptionButtonWidget) widget).visible = fabric_isControlVisible(((ButtonWidget) widget).id);
			}
		}
	}

	@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/OptionButtonWidget;<init>(IIIIILjava/lang/String;)V"), index = 2)
	private int modifyControlY(int y) {
		int temp = y - (this.height / 6);
		int heightOffset = temp / 24;
		return this.height / 6 + 24 * (heightOffset % 7);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo info) {
		fabric_updateSelection();

		buttons.add(new FabricControlsScreenComponents.ControlsButtonWidget(this.width / 2 + 100, this.height / 6 + 168, FabricControlsScreenComponents.Type.NEXT, this));
		buttons.add(new FabricControlsScreenComponents.ControlsButtonWidget(this.width / 2 - 120, this.height / 6 + 168, FabricControlsScreenComponents.Type.PREVIOUS, this));
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/options/ControlsOptionsScreen;drawWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
	private void modifyLabelPos(ControlsOptionsScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original, @Local(index = 5) int id) {
		if (fabric_isControlVisible(id)) {
			int temp = y - (this.height / 6);
			int heightOffset = temp / 24;
			y = this.height / 6 + 24 * (heightOffset % 7) + 7;
			original.call(instance, textRenderer, text, x, y, color);
		}
	}
}
