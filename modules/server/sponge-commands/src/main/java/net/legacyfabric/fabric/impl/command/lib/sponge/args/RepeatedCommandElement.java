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
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class RepeatedCommandElement extends CommandElement {
	private final CommandElement element;
	private final int times;

	public RepeatedCommandElement(CommandElement element, int times) {
		super(null);
		this.element = element;
		this.times = times;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		for (int i = 0; i < this.times; ++i) {
			this.element.parse(source, args, context);
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		for (int i = 0; i < this.times; ++i) {
			CommandArgs.Snapshot startState = args.getSnapshot();

			try {
				this.element.parse(src, args, context);
			} catch (ArgumentParseException e) {
				args.applySnapshot(startState);
				return this.element.complete(src, args, context);
			}
		}

		return Collections.emptyList();
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		return new LiteralText(this.times + '*' + this.element.getUsage(src).getString());
	}
}
