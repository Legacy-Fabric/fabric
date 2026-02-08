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

package net.legacyfabric.fabric.mixin.item.group.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.inventory.menu.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.inventory.menu.PlayerInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.inventory.menu.InventoryMenu;
import net.minecraft.item.CreativeModeTab;

import net.legacyfabric.fabric.impl.item.group.CreativeGuiExtensions;
import net.legacyfabric.fabric.impl.item.group.FabricCreativeGuiComponents;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativePlayerInventoryGui extends PlayerInventoryScreen implements CreativeGuiExtensions {
	public MixinCreativePlayerInventoryGui(InventoryMenu screenHandler) {
		super(screenHandler);
	}

	@Shadow
	protected abstract void setSelectedTab(CreativeModeTab itemGroup_1);

	@Shadow
	public abstract int getSelectedTab(); /* XXX getSelectedTab XXX */

	// "static" matches selectedTab
	private static int fabric_currentPage = 0;

	private int fabric_getPageOffset(int page) {
		switch (page) {
		case 0:
			return 0;
		case 1:
			return 12;
		default:
			return 12 + ((12 - FabricCreativeGuiComponents.COMMON_GROUPS.size()) * (page - 1));
		}
	}

	private int fabric_getOffsetPage(int offset) {
		if (offset < 12) {
			return 0;
		} else {
			return 1 + ((offset - 12) / (12 - FabricCreativeGuiComponents.COMMON_GROUPS.size()));
		}
	}

	@Override
	public void fabric_nextPage() {
		if (fabric_getPageOffset(fabric_currentPage + 1) >= CreativeModeTab.ALL.length) {
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
	public boolean fabric_isButtonVisible(FabricCreativeGuiComponents.Type type) {
		return CreativeModeTab.ALL.length > 12;
	}

	@Override
	public boolean fabric_isButtonEnabled(FabricCreativeGuiComponents.Type type) {
		if (type == FabricCreativeGuiComponents.Type.NEXT) {
			return !(fabric_getPageOffset(fabric_currentPage + 1) >= CreativeModeTab.ALL.length);
		}

		if (type == FabricCreativeGuiComponents.Type.PREVIOUS) {
			return fabric_currentPage != 0;
		}

		return false;
	}

	@Inject(at = @At("TAIL"), method = "buttonClicked")
	private void interceptClick(ButtonWidget button, CallbackInfo ci) {
		if (button instanceof FabricCreativeGuiComponents.ItemGroupButtonWidget) {
			((FabricCreativeGuiComponents.ItemGroupButtonWidget) button).click();
		}
	}

	private void fabric_updateSelection() {
		int minPos = fabric_getPageOffset(fabric_currentPage);
		int maxPos = fabric_getPageOffset(fabric_currentPage + 1) - 1;
		int curPos = getSelectedTab();

		if (curPos < minPos || curPos > maxPos) {
			setSelectedTab(CreativeModeTab.ALL[fabric_getPageOffset(fabric_currentPage)]);
		}
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo info) {
		fabric_updateSelection();

		int xpos = x + (FabricCreativeGuiComponents.hasHotBar ? 116 : 145);
		int ypos = y - 10;

		buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(xpos + 11, ypos, FabricCreativeGuiComponents.Type.NEXT, this));
		buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(xpos, ypos, FabricCreativeGuiComponents.Type.PREVIOUS, this));
	}

	@Inject(method = "setSelectedTab", at = @At("HEAD"), cancellable = true)
	private void setSelectedTab(CreativeModeTab itemGroup, CallbackInfo info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	@Inject(method = "renderTabTooltipIfHovered", at = @At("HEAD"), cancellable = true)
	private void renderTabTooltipIfHovered(CreativeModeTab itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "isInRow", at = @At("HEAD"), cancellable = true)
	private void isClickInTab(CreativeModeTab itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "renderTabIcon", at = @At("HEAD"), cancellable = true)
	private void renderTabIcon(CreativeModeTab itemGroup, CallbackInfo info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	private boolean fabric_isGroupVisible(CreativeModeTab itemGroup) {
		if (FabricCreativeGuiComponents.COMMON_GROUPS.contains(itemGroup)) {
			return true;
		}

		return fabric_currentPage == fabric_getOffsetPage(itemGroup.getId());
	}

	@Override
	public int fabric_currentPage() {
		return fabric_currentPage;
	}
}
