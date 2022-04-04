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

		return new LiteralText(ret.getString());
	}
}
