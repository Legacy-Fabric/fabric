/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class FirstParsingCommandElement extends CommandElement {
	private final List<CommandElement> elements;

	public FirstParsingCommandElement(List<CommandElement> elements) {
		super(null);
		this.elements = elements;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		ArgumentParseException lastException = null;

		for (CommandElement element : this.elements) {
			CommandArgs.Snapshot startState = args.getSnapshot();
			CommandContext.Snapshot contextSnapshot = context.createSnapshot();

			try {
				element.parse(source, args, context);
				return;
			} catch (ArgumentParseException ex) {
				lastException = ex;
				args.applySnapshot(startState);
				context.applySnapshot(contextSnapshot);
			}
		}

		if (lastException != null) {
			throw lastException;
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(final PermissibleCommandSource src, final CommandArgs args, final CommandContext context) {
		return Lists.newLinkedList(Iterables.concat(this.elements.stream().map(element -> {
			if (element == null) {
				return ImmutableList.<String>of();
			} else {
				CommandArgs.Snapshot snapshot = args.getSnapshot();
				List<String> ret = element.complete(src, args, context);
				args.applySnapshot(snapshot);
				return ret;
			}
		}).collect(Collectors.toSet())));
	}

	@Override
	public Text getUsage(PermissibleCommandSource commander) {
		final Text ret = new LiteralText("");

		for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext(); ) {
			ret.append(it.next().getUsage(commander));

			if (it.hasNext()) {
				ret.append(CommandMessageFormatting.PIPE_TEXT);
			}
		}

		return new LiteralText(ret.asUnformattedString());
	}
}
