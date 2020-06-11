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

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.util.EntityHitResult;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "onPlayerInteractEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D", shift = At.Shift.AFTER), cancellable = true)
	public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo info) {
		World world = player.getServerWorld();
		Entity entity = packet.getEntity(world);

		if (entity != null) {
			EntityHitResult hitResult = new EntityHitResult(entity, packet.getHitPosition().add(entity.x, entity.y, entity.z));

			ActionResult result = UseEntityCallback.EVENT.invoker().interact(player, world, entity, hitResult);

			if (result != ActionResult.PASS) {
				info.cancel();
			}
		}
	}
}
