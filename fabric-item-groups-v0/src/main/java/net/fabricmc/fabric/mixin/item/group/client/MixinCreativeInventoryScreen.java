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

package net.fabricmc.fabric.mixin.item.group.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.container.Container;
import net.minecraft.item.ItemGroup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.item.group.CreativeInventoryScreenExtensions;
import net.fabricmc.fabric.impl.item.group.FabricCreativeGuiComponents;

@Environment(EnvType.CLIENT)
@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen extends InventoryScreen implements CreativeInventoryScreenExtensions {
	@Shadow
	protected abstract void setSelectedTab(ItemGroup group);

	@Shadow
	public abstract int getSelectedTab();

	@Unique
	private static int fabric_currentPage = 0;

	public MixinCreativeInventoryScreen(Container container) {
		super(container);
	}

	@Unique
	private int getPageOffset(int page) {
		switch (page) {
			case 0:
				return 0;
			case 1:
				return 12;
			default:
				return 12 + ((12 - FabricCreativeGuiComponents.COMMON_GROUPS.size()) * (page - 1));
		}
	}

	@Unique
	private int getOffsetPage(int offset) {
		if (offset < 12) {
			return 0;
		} else {
			return 1 + ((offset - 12) / (12 - FabricCreativeGuiComponents.COMMON_GROUPS.size()));
		}
	}

	@Unique
	private void fabric_updateSelection() {
		int minPos = this.getOffsetPage(fabric_currentPage);
		int maxPos = this.getPageOffset(fabric_currentPage + 1) - 1;
		int curPos = getSelectedTab();

		if (curPos < minPos || curPos > maxPos) {
			setSelectedTab(ItemGroup.itemGroups[this.getPageOffset(fabric_currentPage)]);
		}
	}

	@Unique
	private boolean fabric_isGroupNotVisible(ItemGroup itemGroup) {
		if (FabricCreativeGuiComponents.COMMON_GROUPS.contains(itemGroup)) {
			return false;
		}

		return fabric_currentPage != this.getOffsetPage(itemGroup.getIndex());
	}

	@Override
	public void fabric_nextPage() {
		if (this.getPageOffset(fabric_currentPage + 1) >= ItemGroup.itemGroups.length) {
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
	public boolean fabric_isButtonVisible(FabricCreativeGuiComponents.Type type) {
		return ItemGroup.itemGroups.length > 12;
	}

	@Override
	public boolean fabric_isButtonEnabled(FabricCreativeGuiComponents.Type type) {
		if (type == FabricCreativeGuiComponents.Type.NEXT) {
			return !(this.getPageOffset(fabric_currentPage + 1) >= ItemGroup.itemGroups.length);
		}

		if (type == FabricCreativeGuiComponents.Type.PREVIOUS) {
			return fabric_currentPage != 0;
		}

		return false;
	}

	@Inject(method = "init", at = @At("RETURN"), remap = false)
	private void init(CallbackInfo info) {
		fabric_updateSelection();

		int xPos = x + 170;
		int yPos = y + 4;

		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(xPos + 10, yPos, FabricCreativeGuiComponents.Type.NEXT, this));
		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(xPos, yPos, FabricCreativeGuiComponents.Type.PREVIOUS, this));
	}

	@Inject(method = "setSelectedTab", at = @At("HEAD"), cancellable = true)
	private void setSelectedTab(ItemGroup itemGroup, CallbackInfo info) {
		if (this.fabric_isGroupNotVisible(itemGroup)) {
			info.cancel();
		}
	}

	@Inject(method = "renderTabTooltipIfHovered", at = @At("HEAD"), cancellable = true)
	private void renderTabTooltipIfHovered(ItemGroup itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		if (fabric_isGroupNotVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "method_2928", at = @At("HEAD"), cancellable = true)
	private void isMouseHovered(ItemGroup itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		if (fabric_isGroupNotVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "renderTabIcon", at = @At("HEAD"), cancellable = true)
	private void renderTabIcon(ItemGroup itemGroup, CallbackInfo info) {
		if (fabric_isGroupNotVisible(itemGroup)) {
			info.cancel();
		}
	}
}
