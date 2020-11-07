package net.fabricmc.fabric.mixin.worldgen.event.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.worldgen.event.v1.client.BiomeColorEvents;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Inject(method = "getGrassColor", at = @At("TAIL"), cancellable = true)
	private static void callGrassEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		TypedActionResult<Integer> retVal = BiomeColorEvents.GRASS.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}

	@Inject(method = "getFoliageColor", at = @At("TAIL"), cancellable = true)
	private static void callFoliageEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		TypedActionResult<Integer> retVal = BiomeColorEvents.FOLIAGE.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}

	@Inject(method = "getWaterColor", at = @At("TAIL"), cancellable = true)
	private static void callWaterEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		TypedActionResult<Integer> retVal = BiomeColorEvents.FOLIAGE.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}
}
