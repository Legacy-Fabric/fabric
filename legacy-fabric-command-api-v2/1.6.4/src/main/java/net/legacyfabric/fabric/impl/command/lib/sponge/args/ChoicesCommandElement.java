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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.TriState;

public class ChoicesCommandElement extends CommandElement {
	public static final int CUTOFF = 5;
	private final Supplier<Collection<String>> keySupplier;
	private final Function<String, ?> valueSupplier;
	private final TriState choicesInUsage;

	public ChoicesCommandElement(ChatMessage key, Supplier<Collection<String>> keySupplier, Function<String, ?> valueSupplier, TriState choicesInUsage) {
		super(key);
		this.keySupplier = keySupplier;
		this.valueSupplier = valueSupplier;
		this.choicesInUsage = choicesInUsage;
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		Object value = this.valueSupplier.apply(args.next());

		if (value == null) {
			throw args.createError(ChatMessage.createTextMessage(String.format("Argument was not a valid choice. Valid choices: %s", this.keySupplier.get().toString())));
		}

		return value;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		final String prefix = args.nextIfPresent().orElse("");
		return Collections.unmodifiableList(this.keySupplier.get().stream().filter((input) -> input.startsWith(prefix)).collect(Collectors.toList()));
	}

	@Override
	public ChatMessage getUsage(PermissibleCommandSource commander) {
		Collection<String> keys = this.keySupplier.get();

		if (this.choicesInUsage == TriState.TRUE || (this.choicesInUsage == TriState.DEFAULT && keys.size() <= CUTOFF)) {
			final ChatMessage build = ChatMessage.createTextMessage("");
			build.addUsing(CommandMessageFormatting.LT_TEXT);

			for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
				build.addUsing(ChatMessage.createTextMessage(it.next()));

				if (it.hasNext()) {
					build.addUsing(CommandMessageFormatting.PIPE_TEXT);
				}
			}

			build.addUsing(CommandMessageFormatting.GT_TEXT);
			return ChatMessage.createTextMessage(build.toString());
		}

		return super.getUsage(commander);
	}
}
