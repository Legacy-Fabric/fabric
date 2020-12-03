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

package net.fabricmc.fabric.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import net.fabricmc.fabric.api.command.v1.DispatcherRegistrationCallback;
import net.fabricmc.fabric.impl.command.CommandManagerHolder;

@Mixin(CommandManager.class)
public class MixinCommandManager {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void postInitialize(CallbackInfo info) {
		DispatcherRegistrationCallback.EVENT.invoker().initialize(
				CommandManagerHolder.COMMAND_DISPATCHER = new CommandDispatcher<>(),
				MinecraftServer.getServer().isDedicated());
	}
}
