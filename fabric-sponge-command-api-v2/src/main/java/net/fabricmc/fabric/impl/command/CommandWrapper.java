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

package net.fabricmc.fabric.impl.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.command.v2.Location;
import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandException;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandMapping;

class CommandWrapper extends AbstractCommand {
	private final CommandMapping mapping;

	CommandWrapper(CommandMapping mapping) {
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
		return this.mapping.getCallable().getHelp((PermissibleCommandSource) source).map(Text::asString).orElse("");
	}

	@Override
	public void execute(CommandSource source, String[] args) {
		try {
			this.mapping.getCallable().process((PermissibleCommandSource) source, String.join(" ", args));
		} catch (CommandException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

		return Collections.emptyList();
	}
}
