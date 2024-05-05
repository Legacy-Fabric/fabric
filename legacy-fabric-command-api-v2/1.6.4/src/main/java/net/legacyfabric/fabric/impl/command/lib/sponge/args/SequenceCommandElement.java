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
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class SequenceCommandElement extends CommandElement {
	private final List<CommandElement> elements;

	public SequenceCommandElement(List<CommandElement> elements) {
		super(null);
		this.elements = elements;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		for (CommandElement element : this.elements) {
			element.parse(source, args, context);
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		Set<String> completions = Sets.newHashSet();

		for (CommandElement element : this.elements) {
			CommandArgs.Snapshot state = args.getSnapshot();
			CommandContext.Snapshot contextSnapshot = context.createSnapshot();

			try {
				element.parse(src, args, context);

				// If we get here, the parse occurred successfully.
				// However, if nothing was consumed, then we should consider
				// what could have been.
				CommandContext.Snapshot afterSnapshot = context.createSnapshot();

				if (state.equals(args.getSnapshot())) {
					context.applySnapshot(contextSnapshot);
					completions.addAll(element.complete(src, args, context));
					args.applySnapshot(state);
					context.applySnapshot(afterSnapshot);
				} else if (args.hasNext()) {
					completions.clear();
				} else {
					// What we might also have - we have no args left to parse so
					// while the parse itself was successful, there could be other
					// valid entries to add...
					context.applySnapshot(contextSnapshot);
					args.applySnapshot(state);
					completions.addAll(element.complete(src, args, context));

					if (!(element instanceof OptionalCommandElement)) {
						break;
					}

					// The last element was optional, so we go back to before this
					// element would have been parsed, and assume it never existed...
					context.applySnapshot(contextSnapshot);
					args.applySnapshot(state);
				}
			} catch (ArgumentParseException ignored) {
				args.applySnapshot(state);
				context.applySnapshot(contextSnapshot);
				completions.addAll(element.complete(src, args, context));
				break;
			}
		}

		return Lists.newArrayList(completions);
	}

	@Override
	public ChatMessage getUsage(PermissibleCommandSource commander) {
		final ChatMessage build = ChatMessage.createTextMessage("");

		for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext(); ) {
			ChatMessage usage = it.next().getUsage(commander);

			if (!usage.toString().isEmpty()) {
				build.addUsing(usage);

				if (it.hasNext()) {
					build.addUsing(CommandMessageFormatting.SPACE_TEXT);
				}
			}
		}

		return ChatMessage.createTextMessage(build.toString());
	}
}
