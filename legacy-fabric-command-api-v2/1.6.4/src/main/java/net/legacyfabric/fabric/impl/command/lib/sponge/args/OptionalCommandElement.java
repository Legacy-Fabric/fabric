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

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class OptionalCommandElement extends CommandElement {
	private final CommandElement element;
	@Nullable
	private final Object value;
	private final boolean considerInvalidFormatEmpty;

	public OptionalCommandElement(CommandElement element, @Nullable Object value, boolean considerInvalidFormatEmpty) {
		super(null);
		this.element = element;
		this.value = value;
		this.considerInvalidFormatEmpty = considerInvalidFormatEmpty;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		if (!args.hasNext()) {
			ChatMessage key = this.element.getKey();

			if (key != null && this.value != null) {
				context.putArg(key.toString(true), this.value);
			}

			return;
		}

		CommandArgs.Snapshot startState = args.getSnapshot();

		try {
			this.element.parse(source, args, context);
		} catch (ArgumentParseException ex) {
			if (this.considerInvalidFormatEmpty || args.hasNext()) { // If there are more args, suppress. Otherwise, throw the error
				args.applySnapshot(startState);

				if (this.element.getKey() != null && this.value != null) {
					context.putArg(this.element.getUntranslatedKey(), this.value);
				}
			} else {
				throw ex;
			}
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return args.hasNext() ? null : this.element.parseValue(source, args);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		return this.element.complete(src, args, context);
	}

	@Override
	public ChatMessage getUsage(PermissibleCommandSource src) {
		final ChatMessage containingUsage = this.element.getUsage(src);

		if (containingUsage.toString().isEmpty()) {
			return ChatMessage.createTextMessage("");
		}

		return ChatMessage.createTextMessage("[" + this.element.getUsage(src).toString() + "]");
	}
}
