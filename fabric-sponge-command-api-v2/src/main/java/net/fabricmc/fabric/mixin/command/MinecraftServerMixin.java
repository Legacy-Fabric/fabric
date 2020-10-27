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

package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;
import net.fabricmc.fabric.api.command.v2.event.CommandRegistrationCallback;
import net.fabricmc.fabric.impl.command.InternalObjects;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}

	@Inject(at = @At("HEAD"), method = "createCommandManager")
	public void initCommands(CallbackInfoReturnable<CommandManager> cir) {
		CommandRegistrationCallback.EVENT.invoker().register(InternalObjects.getCommandManager(), (MinecraftServer) (Object) this);
	}
}
