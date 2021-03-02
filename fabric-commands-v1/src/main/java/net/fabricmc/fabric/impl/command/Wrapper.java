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

package net.fabricmc.fabric.impl.command;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.BuiltInExceptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import net.fabricmc.fabric.api.command.v1.DispatcherRegistrationCallback;
import net.fabricmc.fabric.api.command.v1.ServerCommandSource;

public class Wrapper extends AbstractCommand {
	private final LiteralCommandNode<ServerCommandSource> inner;

	public Wrapper(LiteralCommandNode<ServerCommandSource> inner) {
		this.inner = inner;
	}

	@Override
	public String getCommandName() {
		return this.inner.getLiteral();
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return this.inner.getUsageText();
	}

	@Override
	public void execute(CommandSource source, String[] args) throws CommandException {
		try {
			String value;

			if (args.length == 0) {
				value = this.getCommandName();
			} else {
				value = this.getCommandName() + " " + String.join(" ", args);
			}

			CommandManagerHolder.COMMAND_DISPATCHER.execute(value, ServerCommandSource.from(source));
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

	public static void afterEvaluate(MinecraftServer server, CommandManager commandManager) {
		CommandDispatcher<ServerCommandSource> dispatcher = CommandManagerHolder.COMMAND_DISPATCHER;
		Preconditions.checkNotNull(dispatcher, "Dispatcher was null");
		DispatcherRegistrationCallback.EVENT.invoker().initialize(dispatcher, server.isDedicated());
		dispatcher.getRoot().getChildren().forEach(node -> {
			if (!(node instanceof LiteralCommandNode)) {
				return;
			}

			commandManager.registerCommand(new Wrapper((LiteralCommandNode<ServerCommandSource>) node));
		});
	}
}
