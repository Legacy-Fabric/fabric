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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.ArgumentParseException;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandArgs;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandContext;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandElement;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;
import org.jetbrains.annotations.Nullable;

public class WithSuggestionsElement extends CommandElement {
	private final CommandElement wrapped;
	private final Function<PermissibleCommandSource, Iterable<String>> suggestions;
	private final boolean requireBegin;

	public WithSuggestionsElement(CommandElement wrapped, Function<PermissibleCommandSource, Iterable<String>> suggestions, boolean requireBegin) {
		super(wrapped.getKey());
		this.wrapped = wrapped;
		this.suggestions = suggestions;
		this.requireBegin = requireBegin;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return this.wrapped.parseValue(source, args);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		if (this.requireBegin) {
			String arg = args.nextIfPresent().orElse("");
			return ImmutableList.copyOf(StreamSupport.stream(this.suggestions.apply(src).spliterator(), false).filter(f -> f.startsWith(arg)).collect(Collectors.toList()));
		} else {
			return ImmutableList.copyOf(this.suggestions.apply(src));
		}
	}
}
