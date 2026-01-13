/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.event.lifecycle.modern.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraft.server.MinecraftServer;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerTickEvents;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getTimeMillis()J"), method = "run", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;running:Z")))
	public long api$startServerTick(Operation<Long> original) {
		long time = original.call();
		ServerTickEvents.START_SERVER_TICK.invoker().onStartTick((MinecraftServer) (Object) this);
		return time;
	}
}
