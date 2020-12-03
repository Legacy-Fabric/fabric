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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import net.fabricmc.fabric.impl.command.CommandManagerHolder;

@Mixin(CommandRegistry.class)
public class MixinCommandRegistry {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private void execute(CommandSource source, String command, CallbackInfoReturnable<Integer> info) {
		CommandDispatcher<ServerCommandSource> dispatcher = CommandManagerHolder.COMMAND_DISPATCHER;
		boolean removeSlash = command.startsWith("/");

		{
			String[] split = (removeSlash ? command.substring(1) : command).split(" ");

			if (split.length < 1 || dispatcher.findNode(Collections.singleton(split[0])) == null) {
				return;
			}
		}

		try {
			StringReader reader = new StringReader(command);

			if (removeSlash) {
				reader.skip();
			}

			info.setReturnValue(dispatcher.execute(reader, ServerCommandSource.from(source)));
		} catch (CommandSyntaxException exception) {
			source.sendMessage(new LiteralText(exception.getMessage()));
			info.setReturnValue(-1);
		}
	}

	@Inject(method = "getCompletions", at = @At("RETURN"), cancellable = true)
	private void getCommandCompletions(CommandSource source, String name, BlockPos pos, CallbackInfoReturnable<List<String>> info) {
		CommandDispatcher<ServerCommandSource> dispatcher = CommandManagerHolder.COMMAND_DISPATCHER;
		List<String> list = info.getReturnValue();

		if (list == null) {
			list = new ArrayList<>();
		}

		StringReader stringReader = new StringReader(name);

		if (stringReader.canRead() && stringReader.peek() == '/') {
			stringReader.skip();
		}

		ParseResults<ServerCommandSource> parseResults = dispatcher.parse(stringReader, ServerCommandSource.from(source));

		try {
			for (Suggestion suggestion : dispatcher.getCompletionSuggestions(parseResults).get(10, TimeUnit.MILLISECONDS).getList()) {
				list.add(suggestion.getText());
			}

			info.setReturnValue(list);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// Ignore
		}
	}
}
