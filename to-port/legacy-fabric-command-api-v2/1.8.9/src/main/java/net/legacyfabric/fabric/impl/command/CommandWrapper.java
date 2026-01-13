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

package net.legacyfabric.fabric.impl.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandPermissionException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.InvocationCommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

public class CommandWrapper extends AbstractCommand {
	private final CommandMapping mapping;

	public CommandWrapper(CommandMapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public String getCommandName() {
		return this.mapping.getPrimaryAlias();
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(this.mapping.getAllAliases());
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return this.mapping.getCallable().getHelp((PermissibleCommandSource) source).map(Text::computeValue).orElse("");
	}

	@Override
	public void execute(CommandSource source, String[] args) {
		try {
			try {
				this.mapping.getCallable().process((PermissibleCommandSource) source, String.join(" ", args));
			} catch (InvocationCommandException e) {
				if (e.getCause() != null) {
					throw e.getCause();
				}
			} catch (CommandPermissionException e) {
				if (e.getText() != null) {
					source.sendMessage(CommandMessageFormatting.error(e.getText()));
				}
			} catch (CommandException e) {
				Text text = e.getText();

				if (text != null) {
					source.sendMessage(CommandMessageFormatting.error(text));
				}

				if (e.shouldIncludeUsage()) {
					Text usage;

					if (e instanceof ArgumentParseException.WithUsage) {
						usage = ((ArgumentParseException.WithUsage) e).getUsage();
					} else {
						usage = this.mapping.getCallable().getUsage((PermissibleCommandSource) source);
					}

					source.sendMessage(CommandMessageFormatting.error(new LiteralText(String.format("Usage: /%s %s", this.getCommandName(), usage.asUnformattedString()))));
				}
			}
		} catch (Throwable t) {
			// Minecraft handles these exceptions for us
			throw new RuntimeException(t);
		}
	}

	@Override
	public boolean isAccessible(CommandSource source) {
		return this.mapping.getCallable().testPermission((PermissibleCommandSource) source);
	}

	@Override
	public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
		try {
			return this.mapping.getCallable().getSuggestions((PermissibleCommandSource) source, Arrays.stream(args).collect(Collectors.joining(" ")), new Location<>(source.getWorld(), pos));
		} catch (CommandException e) {
			source.sendMessage(CommandMessageFormatting.error(new LiteralText(String.format("Error getting suggestions: %s", e.getText().asUnformattedString()))));
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error occurred while providing auto complete hints for '%s'", String.join(" ", args)), e);
		}
	}
}
