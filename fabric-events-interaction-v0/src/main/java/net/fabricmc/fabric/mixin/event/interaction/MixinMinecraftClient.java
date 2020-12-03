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

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.hit.HitResult;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.player.screen.ScreenOpenedCallback;
import net.fabricmc.fabric.impl.base.util.ActionResult;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow
	public HitResult result;

	@Shadow
	public abstract void openScreen(Screen screen);

	@Inject(at = @At("HEAD"), cancellable = true, method = "openScreen")
	public void hookOpenScreen(Screen screen, CallbackInfo ci) {
		AtomicReference<Screen> screenReference = new AtomicReference<>(screen);
		ActionResult result = ScreenOpenedCallback.EVENT.invoker().onScreenOpened(screenReference);

		if (result != ActionResult.PASS) {
			if (result == ActionResult.FAIL) {
				ci.cancel();
			} else if (result == ActionResult.SUCCESS && screen != screenReference.get()) {
				this.openScreen(screenReference.get());
			}
		}
	}
}
