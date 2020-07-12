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

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.impl.command.CommandSide;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;

import net.fabricmc.fabric.impl.command.FabricCommandRegistryImpl;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager extends CommandRegistry implements CommandProvider {
	@Inject(method = "<init>", at = @At("TAIL"))
	public void registerCommands(CallbackInfo info){
		FabricCommandRegistryImpl.getCommandMap().forEach((command, side)->{
			boolean dedicated = MinecraftServer.getServer().isDedicated();
			if (!(dedicated) && side == CommandSide.CLIENT) {
				this.registerCommand(command);
			}
			else if (dedicated && side == CommandSide.SERVER){
				this.registerCommand(command);
			}
			else {
				this.registerCommand(command);
			}

			AbstractCommand.setCommandProvider(this);
		});
	}
}
