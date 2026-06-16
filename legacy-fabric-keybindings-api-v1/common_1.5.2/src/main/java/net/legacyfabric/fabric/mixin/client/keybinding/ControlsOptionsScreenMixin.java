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

package net.legacyfabric.fabric.mixin.client.keybinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.TextRenderer;

import net.legacyfabric.fabric.impl.client.keybinding.ControlsScreenExtensions;
import net.legacyfabric.fabric.impl.client.keybinding.FabricControlsScreenComponents;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends Screen implements ControlsScreenExtensions {
	@Shadow
	private GameOptions options;

	@Shadow
	protected abstract int getControlsListX();

	@Unique
	private int fabric_currentPage = 0;

	@Unique
	private List<String> categories = new ArrayList<>();

	@Unique
	private Int2IntMap bindToVirtualIndex = new Int2IntArrayMap();

	@Unique
	private Object2IntMap<String> bindPerCategory = new Object2IntArrayMap<>();

	@Unique
	private int maxPageOffset;

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
		String category = this.options.keyBindings[offset].getCategory();
		int categoryIndex = categories.indexOf(category);
		int virtualIndex = bindToVirtualIndex.get(offset);

		int pageOffset;

		if (virtualIndex < FabricControlsScreenComponents.AMOUNT_PER_PAGE) {
			pageOffset = 0;
		} else {
			pageOffset = 1 + ((virtualIndex - FabricControlsScreenComponents.AMOUNT_PER_PAGE) / FabricControlsScreenComponents.AMOUNT_PER_PAGE);
		}

		int tempCat = categoryIndex;

		while (tempCat > 0) {
			tempCat--;
			int tempTotalInCategory = bindPerCategory.getInt(categories.get(tempCat));
			pageOffset += (int) Math.ceil((double) tempTotalInCategory / FabricControlsScreenComponents.AMOUNT_PER_PAGE);
		}

		return pageOffset;
	}

	private String fabric_getCategoryForPage() {
		int pageOffset = 0;

		for (String category : categories) {
			int tempTotalInCategory = bindPerCategory.getInt(category);
			pageOffset += (int) Math.ceil((double) tempTotalInCategory / FabricControlsScreenComponents.AMOUNT_PER_PAGE);

			if (pageOffset > fabric_currentPage) {
				return category;
			}
		}

		return null;
	}

	@Override
	public boolean fabric_isButtonVisible(FabricControlsScreenComponents.Type type) {
		return this.options.keyBindings.length > FabricControlsScreenComponents.AMOUNT_PER_PAGE;
	}

	@Override
	public void fabric_nextPage() {
		if (fabric_getPageOffset(fabric_currentPage + 1) >= fabric_getPageOffset(maxPageOffset)) {
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
			return !(fabric_getPageOffset(fabric_currentPage + 1) >= fabric_getPageOffset(maxPageOffset));
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

	@Inject(method = "init", at = @At("HEAD"))
	private void collectCategories(CallbackInfo ci) {
		fabric_currentPage = 0;
		categories.clear();
		bindToVirtualIndex.clear();
		bindPerCategory.clear();

		Set<String> cat = new HashSet<>();
		for (KeyBinding keyBinding : this.options.keyBindings) {
			cat.add(keyBinding.getCategory());
		}

		categories.addAll(cat);
		categories.remove("Vanilla");
		categories.add(0, "Vanilla");

		for (int i = 0; i < this.options.keyBindings.length; i++) {
			KeyBinding keyBinding = this.options.keyBindings[i];
			if (!bindPerCategory.containsKey(keyBinding.getCategory())) {
				bindPerCategory.put(keyBinding.getCategory(), 0);
			}

			bindToVirtualIndex.put(i, bindPerCategory.getInt(keyBinding.getCategory()));

			bindPerCategory.put(keyBinding.getCategory(), bindPerCategory.getInt(keyBinding.getCategory()) + 1);
		}

		maxPageOffset = 0;
		for (String category : categories) {
			int tempTotalInCategory = bindPerCategory.getInt(category);
			maxPageOffset += (int) Math.ceil((double) tempTotalInCategory / FabricControlsScreenComponents.AMOUNT_PER_PAGE);
		}
	}

	@Definition(id = "OptionButtonWidget", type = OptionButtonWidget.class)
	@Expression("new OptionButtonWidget(?, ?, ?, ?, ?, ?)")
	@WrapOperation(method = "init", at = @At("MIXINEXTRAS:EXPRESSION"))
	private OptionButtonWidget fabric_createButton(int i, int j, int k, int l, int m, String string, Operation<OptionButtonWidget> original) {
		int var2 = this.getControlsListX();
		int virtualIndex = bindToVirtualIndex.get(i);
		int temp = (this.height / 6 + 24 * (virtualIndex >> 1)) - (this.height / 6);
		int heightOffset = temp / 24;
		int resultingY = this.height / 6 + 24 * (heightOffset % (FabricControlsScreenComponents.AMOUNT_PER_PAGE / 2));
		return original.call(i, var2 + virtualIndex % 2 * 160, resultingY, l, m, string);
	}

	@Inject(method = "init()V", at = @At("RETURN"))
	private void init(CallbackInfo info) {
		fabric_updateSelection();

		buttons.add(new FabricControlsScreenComponents.ControlsButtonWidget(this.width / 2 + 100, this.height / 6 + 168, FabricControlsScreenComponents.Type.NEXT, this));
		buttons.add(new FabricControlsScreenComponents.ControlsButtonWidget(this.width / 2 - 120, this.height / 6 + 168, FabricControlsScreenComponents.Type.PREVIOUS, this));
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/options/ControlsOptionsScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
	private void modifyTitle(ControlsOptionsScreen instance, TextRenderer textRenderer, String s, int i, int j, int k, Operation<Void> original) {
		original.call(instance, textRenderer, s + " - " + fabric_getCategoryForPage(), i, j, k);
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/options/ControlsOptionsScreen;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
	private void modifyLabelPos(ControlsOptionsScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original, @Local(index = 5) int id) {
		if (fabric_isControlVisible(id)) {
			int offsetPerPage = FabricControlsScreenComponents.AMOUNT_PER_PAGE / 2;
			int var2 = this.getControlsListX();
			int virtualIndex = bindToVirtualIndex.get(id);
			int temp = (this.height / 6 + 24 * (virtualIndex >> 1)) - (this.height / 6);
			int heightOffset = temp / 24;
			y = this.height / 6 + 24 * (heightOffset % offsetPerPage) + offsetPerPage;
			original.call(instance, textRenderer, text, var2 + virtualIndex % 2 * 160 + 70 + 6, y, color);
		}
	}
}
