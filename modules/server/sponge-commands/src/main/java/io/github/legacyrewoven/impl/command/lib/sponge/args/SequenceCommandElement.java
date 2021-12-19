/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.legacyrewoven.impl.command.lib.sponge.args;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.legacyrewoven.api.command.v2.lib.sponge.CommandMessageFormatting;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.ArgumentParseException;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandArgs;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandContext;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandElement;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
	public Text getUsage(PermissibleCommandSource commander) {
		final Text build = new LiteralText("");

		for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext(); ) {
			Text usage = it.next().getUsage(commander);

			if (!usage.getString().isEmpty()) {
				build.append(usage);

				if (it.hasNext()) {
					build.append(CommandMessageFormatting.SPACE_TEXT);
				}
			}
		}

		return new LiteralText(build.getString());
	}
}
