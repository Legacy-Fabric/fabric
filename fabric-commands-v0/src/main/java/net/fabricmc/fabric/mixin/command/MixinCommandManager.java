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

import net.fabricmc.fabric.api.registry.FabricCommandRegistry;
import net.fabricmc.fabric.impl.command.CommandSide;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class MixinCommandManager extends CommandRegistry {
	@Inject(method = "<init>", at = @At(value = "INVOKE",target = "Lnet/minecraft/command/AbstractCommand;setCommandProvider(Lnet/minecraft/command/CommandProvider;)V"))
	public void registerCommands(CallbackInfo info){
		FabricCommandRegistry.FABRIC_COMMANDS.forEach((command, side)->{
			if(side == CommandSide.BOTH && command != null){
				this.registerCommand(command);
			}
		});
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/PublishCommand;<init>()V"))
	public void registerClientCommands(CallbackInfo info){
		FabricCommandRegistry.FABRIC_COMMANDS.forEach((command, side)->{
			if(side == CommandSide.CLIENT && command != null){
				this.registerCommand(command);
			}
		});
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/command/OpCommand;<init>()V"))
	public void registerServerCommands(CallbackInfo info){
		FabricCommandRegistry.FABRIC_COMMANDS.forEach((command, side)->{
			if(side == CommandSide.SERVER && command != null){
				this.registerCommand(command);
			}
		});
	}
}
