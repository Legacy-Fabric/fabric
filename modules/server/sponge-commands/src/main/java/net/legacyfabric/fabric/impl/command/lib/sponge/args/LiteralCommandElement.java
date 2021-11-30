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
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class LiteralCommandElement extends CommandElement {
	private final List<String> expectedArgs;
	@Nullable
	private final Object putValue;

	public LiteralCommandElement(@Nullable Text key, List<String> expectedArgs, @Nullable Object putValue) {
		super(key);
		this.expectedArgs = ImmutableList.copyOf(expectedArgs);
		this.putValue = putValue;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		for (String arg : this.expectedArgs) {
			String current;

			if (!(current = args.next()).equalsIgnoreCase(arg)) {
				throw args.createError(new LiteralText(String.format("Argument %s did not match expected next argument %s", current, arg)));
			}
		}

		return this.putValue;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		for (String arg : this.expectedArgs) {
			final Optional<String> next = args.nextIfPresent();

			if (!next.isPresent()) {
				break;
			} else if (args.hasNext()) {
				if (!next.get().equalsIgnoreCase(arg)) {
					break;
				}
			} else {
				if (arg.toLowerCase().startsWith(next.get().toLowerCase())) { // Case-insensitive compare
					return ImmutableList.of(arg); // TODO: Possibly complete all remaining args? Does that even work
				}
			}
		}

		return ImmutableList.of();
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		return new LiteralText(Joiner.on(' ').join(this.expectedArgs));
	}
}
