package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class MixinItem {
	@Inject(method = "method_8265", at = @At("RETURN"))
	private void getTooltip(ItemStack itemStack, PlayerEntity playerEntity, List<String> list, boolean bl, CallbackInfo info) {
		ItemTooltipCallback.EVENT.invoker().getTooltip(new ItemStack((Item)(Object)this), playerEntity, list);
	}
}
