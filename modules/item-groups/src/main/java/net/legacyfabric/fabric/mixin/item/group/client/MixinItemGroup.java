package net.legacyfabric.fabric.mixin.item.group.client;

import net.legacyfabric.fabric.impl.item.group.FabricCreativeGuiComponents;
import net.minecraft.item.itemgroup.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemGroup.class)
public abstract class MixinItemGroup {
	@Shadow
	public abstract int getIndex();

	@Shadow
	public abstract boolean isTopRow();

	@Inject(method = "isTopRow", cancellable = true, at = @At("HEAD"))
	private void isTopRow(CallbackInfoReturnable<Boolean> info) {
		if (getIndex() > 10) {
			info.setReturnValue((getIndex() - 11) % (11 - FabricCreativeGuiComponents.COMMON_GROUPS.size()) < 3	);
		}
	}

	@Inject(method = "getColumn", cancellable = true, at = @At("HEAD"))
	private void getColumn(CallbackInfoReturnable<Integer> info) {
		if (getIndex() > 10) {
			if (isTopRow()) {
				info.setReturnValue((getIndex() - 11) % (11 - FabricCreativeGuiComponents.COMMON_GROUPS.size()));
			} else {
				info.setReturnValue((getIndex() - 11) % (11 - FabricCreativeGuiComponents.COMMON_GROUPS.size()) - 3);
			}
		}
	}
}
