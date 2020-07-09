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

package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientStartCallback;
import net.fabricmc.fabric.api.event.client.ClientStopCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Inject(at = @At("RETURN"), method = "tick")
	public void tick(CallbackInfo info) {
		ClientTickCallback.EVENT.invoker().tick((MinecraftClient) (Object) this);
	}

	@Inject(at = @At("HEAD"),method = "scheduleStop")
	public void stop(CallbackInfo info){
		ClientStopCallback.EVENT.invoker().onStopClient((MinecraftClient) (Object) this);
	}

	@Inject(at = @At("HEAD"),method = "run")
	public void start(CallbackInfo info){
		ClientStartCallback.EVENT.invoker().onStartClient((MinecraftClient) (Object) this);
	}
}
