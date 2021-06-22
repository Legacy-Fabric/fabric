/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.mixin.screen;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.LevelInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import net.legacyfabric.fabric.api.client.screen.v1.ScreenEvents;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
	@Shadow
	public Screen currentScreen;

	@Unique
	private Screen tickingScreen;

	@Inject(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;removed()V", shift = At.Shift.AFTER))
	private void onScreenRemove(@Nullable Screen screen, CallbackInfo ci) {
		ScreenEvents.remove(this.currentScreen).invoker().onRemove(this.currentScreen);
	}

	@Inject(method = "stop", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;destroy()V"))
	private void onDisplayDestroy(CallbackInfo ci) {
		ScreenEvents.remove(this.currentScreen).invoker().onRemove(this.currentScreen);
	}

	// Synthetic method in `tick`
	// These two injections should be caught by "Screen#wrapScreenError" if anything fails in an event and then rethrown in the crash report
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;tick()V"))
	private void beforeScreenTick(CallbackInfo ci) {
		// Store the screen in a variable in case someone tries to change the screen during this before tick event.
		// If someone changes the screen, the after tick event will likely have class cast exceptions or an NPE.
		this.tickingScreen = this.currentScreen;
		ScreenEvents.beforeTick(this.tickingScreen).invoker().beforeTick(this.tickingScreen);
	}

	// Synthetic method in `tick`
	@Inject(method = "tick", at = @At("TAIL"))
	private void afterScreenTick(CallbackInfo ci) {
		ScreenEvents.afterTick(this.tickingScreen).invoker().afterTick(this.tickingScreen);
		// Finally set the currently ticking screen to null
		this.tickingScreen = null;
	}

	// The LevelLoadingScreen is the odd screen that isn't ticked by the main tick loop, so we fire events for this screen.
	// We Coerce the package-private inner class representing the world load action so we don't need an access widener.
	@Inject(method = "tick", at = @At(value = "HEAD", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;tick()V"))
	private void beforeLoadingScreenTick(CallbackInfo ci) {
		// Store the screen in a variable in case someone tries to change the screen during this before tick event.
		// If someone changes the screen, the after tick event will likely have class cast exceptions or throw a NPE.
		this.tickingScreen = this.currentScreen;
		ScreenEvents.beforeTick(this.tickingScreen).invoker().beforeTick(this.tickingScreen);
	}

	@Inject(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;getServerId()Ljava/lang/String;"))
	private void afterLoadingScreenTick(String name, String displayName, LevelInfo levelInfo, CallbackInfo ci) {
		ScreenEvents.afterTick(this.tickingScreen).invoker().afterTick(this.tickingScreen);
		// Finally set the currently ticking screen to null
		this.tickingScreen = null;
	}
}
