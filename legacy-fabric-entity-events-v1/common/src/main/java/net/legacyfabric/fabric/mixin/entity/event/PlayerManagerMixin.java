/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.mixin.entity.event;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;

import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	/**
	 * This is called by both "moveToWorld" and "teleport".
	 * So this is suitable to handle the after event from both call sites.
	 */
	@Inject(method = "teleportToDimension", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void afterWorldChanged(ServerPlayerEntity player, int dimension, CallbackInfo ci, int i, ServerWorld serverWorld, ServerWorld serverWorld2, Iterator iterator) {
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.invoker().afterChangeWorld(player, serverWorld, serverWorld2);
	}
}
