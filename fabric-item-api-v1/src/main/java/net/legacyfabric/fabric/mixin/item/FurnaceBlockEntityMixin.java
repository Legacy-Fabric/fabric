package net.legacyfabric.fabric.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.legacyfabric.fabric.api.item.v1.Fuel;

@Mixin(value = FurnaceBlockEntity.class, priority = 500)
public class FurnaceBlockEntityMixin {
	@Inject(cancellable = true, method = "method_26989", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void modifyBurnTime(ItemStack arg, CallbackInfoReturnable<Integer> cir, Item lv) {
		if (lv instanceof Fuel) {
			cir.setReturnValue(((Fuel) lv).getBurnTime(arg));
		}
	}
}
