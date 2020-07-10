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

package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
	@Shadow
	public ServerPlayerEntity player;

	@Shadow
	public World world;

	@Inject(at = @At("HEAD"), method = "processBlockBreakingAction", cancellable = true)
	public void startBlockBreak(BlockPos pos, Direction direction, CallbackInfo info) {
		ActionResult result = AttackBlockCallback.EVENT.invoker().interact(player, world, pos, direction);

		if (result != ActionResult.PASS) {
			// The client might have broken the block on its side, so make sure to let it know.
			this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(world, pos));
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "method_6091", cancellable = true)
	public void interactBlock(PlayerEntity playerEntity, World world, ItemStack itemStack, BlockPos blockPos, Direction direction, float f, float g, float h, CallbackInfoReturnable<ActionResult> info) {
		ActionResult result = UseBlockCallback.EVENT.invoker().interact(player, world, new HitResult(HitResult.Type.MISS, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), direction, blockPos));

		if (result != ActionResult.PASS) {
			info.setReturnValue(result);
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "method_6090", cancellable = true)
	public void interactItem(PlayerEntity player, World world, ItemStack stack, CallbackInfoReturnable<ActionResult> info) {
		TypedActionResult<ItemStack> result = UseItemCallback.EVENT.invoker().interact(player, world);

		if (result.getResult() != ActionResult.PASS) {
			info.setReturnValue(result.getResult());
			info.cancel();
		}
	}
}
