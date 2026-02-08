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

package net.legacyfabric.fabric.mixin.event.lifecycle.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerWorldEvents;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public ServerWorld[] worlds;

	@Inject(at = @At(value = "TAIL"), method = "deleteWorldAndStop")
	public void serverWorldLoad(CallbackInfo ci) {
		for (ServerWorld world : this.worlds) {
			ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer) (Object) this, world);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setStatus(Lnet/minecraft/server/ServerStatus;)V", shift = At.Shift.AFTER), method = "run")
	public void api$afterServerStart(CallbackInfo ci) {
		ServerLifecycleEvents.SERVER_STARTED.invoker().onServerStarted((MinecraftServer) (Object) this);
	}
}
