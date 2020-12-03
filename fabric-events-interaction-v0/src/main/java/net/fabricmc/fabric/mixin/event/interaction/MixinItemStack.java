/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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
	@Inject(at = @At("HEAD"), method = "use", cancellable = true)
	public void blockPlaced (PlayerEntity playerEntity, World world, BlockPos blockPos, Direction direction, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> info) {
		ActionResult result = PlaceBlockCallback.EVENT.invoker().blockPlaced(playerEntity, world, blockPos, direction, hitX, hitY, hitZ);

		if (result == ActionResult.FAIL) {
			info.setReturnValue(false);
		}
	}
}
