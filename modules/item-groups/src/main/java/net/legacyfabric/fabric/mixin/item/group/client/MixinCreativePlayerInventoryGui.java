package net.legacyfabric.fabric.mixin.item.group.client;

import net.legacyfabric.fabric.impl.item.group.CreativeGuiExtensions;
import net.legacyfabric.fabric.impl.item.group.FabricCreativeGuiComponents;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
		switch (page) {
			case 0:
				return 0;
			case 1:
				return 11;
			default:
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
		return ItemGroup.itemGroups.length > 11;
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
		System.out.println("DEBUG!");

		fabric_updateSelection();

		int xpos = x + 116;
		int ypos = y - 10;

		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(0, xpos + 11, ypos, FabricCreativeGuiComponents.Type.NEXT, this));
		this.buttons.add(new FabricCreativeGuiComponents.ItemGroupButtonWidget(1, xpos, ypos, FabricCreativeGuiComponents.Type.PREVIOUS, this));
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
