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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

@Mixin(ClientPlayerInteractionManager.class)
@Environment(EnvType.CLIENT)
public class MixinClientPlayerInteractionManager {
	@Final
	@Shadow
	private MinecraftClient client;
	@Final
	@Shadow
	private ClientPlayNetworkHandler networkHandler;
	@Shadow
	private LevelInfo.GameMode gameMode;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelInfo$GameMode;isCreative()Z", ordinal = 0), method = "attackBlock", cancellable = true)
	public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
		ActionResult result = AttackBlockCallback.EVENT.invoker().interact(client.player, client.world, pos, direction);

		if (result != ActionResult.PASS) {
			info.setReturnValue(result == ActionResult.SUCCESS);
			info.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelInfo$GameMode;isCreative()Z", ordinal = 0), method = "updateBlockBreakingProgress", cancellable = true)
	public void method_2902(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
		if (!gameMode.isCreative()) {
			return;
		}

		ActionResult result = AttackBlockCallback.EVENT.invoker().interact(client.player, client.world, pos, direction);

		if (result != ActionResult.PASS) {
			info.setReturnValue(result == ActionResult.SUCCESS);
			info.cancel();
		}
	}

	public void interactItem(PlayerEntity player, World world, CallbackInfoReturnable<ActionResult> info) {
		TypedActionResult<ItemStack> result = UseItemCallback.EVENT.invoker().interact(player, world);

		if (result.getResult() != ActionResult.PASS) {
			if (result.getResult() == ActionResult.SUCCESS) {
				return;
			}

			info.setReturnValue(result.getResult());
			info.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0), method = "attackEntity", cancellable = true)
	public void attackEntity(PlayerEntity player, Entity entity, CallbackInfo info) {
		ActionResult result = AttackEntityCallback.EVENT.invoker().attack(player, player.getWorld() /* TODO */, entity, null);

		if (result != ActionResult.PASS) {
			if (result == ActionResult.SUCCESS) {
				this.networkHandler.sendPacket(new PlayerInteractEntityC2SPacket());
			}

			info.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0), method = "interactEntityAtLocation", cancellable = true)
	public void interactEntityAtLocation(PlayerEntity player, Entity entity, HitResult hitResult, CallbackInfoReturnable<Boolean> info) {
		ActionResult result = UseEntityCallback.EVENT.invoker().interact(player, player.getWorld(), entity, hitResult);

		if (result != ActionResult.PASS) {
			if (result == ActionResult.SUCCESS) {
				BlockPos hitVec = hitResult.getBlockPos().subtract(new Vec3i(entity.x, entity.y, entity.z));
				this.networkHandler.sendPacket(new PlayerInteractEntityC2SPacket(entity, new Vec3d(hitVec.getX(), hitVec.getY(), hitVec.getZ())));
			}

			info.setReturnValue(result.isAccepted());
			info.cancel();
		}
	}
}
