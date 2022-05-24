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

package net.legacyfabric.fabric.mixin.item.group.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.screen.ScreenHandler;

import net.legacyfabric.fabric.impl.itemgroup.CreativeGuiExtensions;
import net.legacyfabric.fabric.impl.itemgroup.FabricCreativeGuiComponents;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativePlayerInventoryGui extends InventoryScreen implements CreativeGuiExtensions {
	public MixinCreativePlayerInventoryGui(ScreenHandler screenHandler) {
		super(screenHandler);
	}

	@Shadow
	protected abstract void setSelectedTab(ItemGroup itemGroup_1);

	@Shadow
	public abstract int getSelectedTab();

	// "static" matches selectedTab
	private static int fabric_currentPage = 0;

	private int fabric_getPageOffset(int page) {
		if (page == 0) {
			return 0;
		} else if (page == 1) {
			return 11;
		} else {
			return 11 + ((11 - FabricCreativeGuiComponents.COMMON_GROUPS.size()) * (page - 1));
		}
	}

	private int fabric_getOffsetPage(int offset) {
		if (offset < 11) {
			return 0;
		} else {
			return 1 + ((offset - 11) / (11 - FabricCreativeGuiComponents.COMMON_GROUPS.size()));
		}
	}

	@Override
	public void fabric_nextPage() {
		if (fabric_getPageOffset(fabric_currentPage + 1) >= ItemGroup.itemGroups.length) {
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
		return ItemGroup.itemGroups.length > 12;
	}

	@Override
	public boolean fabric_isButtonEnabled(FabricCreativeGuiComponents.Type type) {
		if (type == FabricCreativeGuiComponents.Type.NEXT) {
			return !(fabric_getPageOffset(fabric_currentPage + 1) >= ItemGroup.itemGroups.length);
		}

		if (type == FabricCreativeGuiComponents.Type.PREVIOUS) {
			return fabric_currentPage != 0;
		}

		return false;
	}

	private void fabric_updateSelection() {
		int minPos = fabric_getPageOffset(fabric_currentPage);
		int maxPos = fabric_getPageOffset(fabric_currentPage + 1) - 1;
		int curPos = getSelectedTab();

		if (curPos < minPos || curPos > maxPos) {
			setSelectedTab(ItemGroup.itemGroups[fabric_getPageOffset(fabric_currentPage)]);
		}
	}

	@Inject(method = "init", at = @At("RETURN"), remap = false)
	private void init(CallbackInfo info) {
		fabric_updateSelection();

		int xpos = x + 145;
		int ypos = y - 10;

		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(202, xpos + 10, ypos, FabricCreativeGuiComponents.Type.NEXT, this));
		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(203, xpos, ypos, FabricCreativeGuiComponents.Type.PREVIOUS, this));
	}

	@Override
	public void buttonClicked(ButtonWidget button) {
		if (button instanceof FabricCreativeGuiComponents.ItemGroupButtonWidget) {
			if (((FabricCreativeGuiComponents.ItemGroupButtonWidget) button).type == FabricCreativeGuiComponents.Type.PREVIOUS) {
				fabric_previousPage();
			} else {
				fabric_nextPage();
			}
		} else {
			super.buttonClicked(button);
		}
	}

	@Inject(method = "setSelectedTab", at = @At("HEAD"), cancellable = true)
	private void setSelectedTab(ItemGroup itemGroup, CallbackInfo info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	@Inject(method = "renderTabTooltipIfHovered", at = @At("HEAD"), cancellable = true)
	private void renderTabTooltipIfHovered(ItemGroup itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "isClickInTab", at = @At("HEAD"), cancellable = true)
	private void isClickInTab(ItemGroup group, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> info) {
		if (!fabric_isGroupVisible(group)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "renderTabIcon", at = @At("HEAD"), cancellable = true)
	private void renderTabIcon(ItemGroup itemGroup, CallbackInfo info) {
		if (!fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	private boolean fabric_isGroupVisible(ItemGroup itemGroup) {
		if (FabricCreativeGuiComponents.COMMON_GROUPS.contains(itemGroup)) {
			return true;
		}

		return fabric_currentPage == fabric_getOffsetPage(itemGroup.getIndex());
	}

	@Override
	public int fabric_currentPage() {
		return fabric_currentPage;
	}
}
