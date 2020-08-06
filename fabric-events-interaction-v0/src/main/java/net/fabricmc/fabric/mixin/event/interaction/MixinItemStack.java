package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.player.PlaceBlockCallback;
import net.fabricmc.fabric.impl.base.util.ActionResult;

@Mixin(ItemStack.class)
public class MixinItemStack {
	@Inject(at = @At("HEAD"), method = "method_8347", cancellable = true)
	public void blockPlaced(PlayerEntity playerEntity, World world, BlockPos blockPos, Direction direction, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> info) {
		ActionResult result = PlaceBlockCallback.EVENT.invoker().blockPlaced(playerEntity, world, blockPos, direction, hitX , hitY, hitZ);

		if (result == ActionResult.FAIL) {
			info.setReturnValue(false);
		}
	}
}
