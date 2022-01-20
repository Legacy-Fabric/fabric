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

import java.util.Collections;
import java.util.List;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class AllOfCommandElement extends CommandElement {
	private final CommandElement element;

	public AllOfCommandElement(CommandElement element) {
		super(null);
		this.element = element;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		while (args.hasNext()) {
			this.element.parse(source, args, context);
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		CommandArgs.Snapshot startState = null;

		try {
			while (args.hasNext()) {
				startState = args.getSnapshot();
				this.element.parse(src, args, context);
			}
		} catch (ArgumentParseException e) {
			// ignored
		}

		// The final element, if an exception was not thrown, might have more completions available to it.
		// Therefore, we reapply the last snapshot and complete from it.
		if (startState != null) {
			args.applySnapshot(startState);
		}

		if (args.canComplete()) {
			return this.element.complete(src, args, context);
		}

		// If we have more elements, do not complete.
		return Collections.emptyList();
	}

	@Override
	public Text getUsage(PermissibleCommandSource context) {
		return new LiteralText(this.element.getUsage(context).getString() + CommandMessageFormatting.STAR_TEXT.asString());
	}
}
