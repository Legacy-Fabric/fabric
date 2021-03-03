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
import net.fabricmc.fabric.api.util.IntActionResult;
import net.fabricmc.fabric.api.worldgen.event.v1.client.BiomeColorEvents;
import net.fabricmc.fabric.impl.base.util.ActionResult;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Inject(method = "getGrassColor", at = @At("TAIL"), cancellable = true)
	private static void callGrassEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		IntActionResult retVal = BiomeColorEvents.GRASS.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}

	@Inject(method = "getFoliageColor", at = @At("TAIL"), cancellable = true)
	private static void callFoliageEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		IntActionResult retVal = BiomeColorEvents.FOLIAGE.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}

	@Inject(method = "getWaterColor", at = @At("TAIL"), cancellable = true)
	private static void callWaterEvent(WorldView view, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		IntActionResult retVal = BiomeColorEvents.FOLIAGE.invoker().apply(cir.getReturnValue(), view, pos, view.getBiomeAt(pos));

		if (retVal.getResult() == ActionResult.SUCCESS) {
			cir.setReturnValue(retVal.getValue());
		}
	}
}
