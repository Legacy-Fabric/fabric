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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class DateTimeElement extends CommandElement {
	private final boolean returnNow;

	public DateTimeElement(Text key, boolean returnNow) {
		super(key);
		this.returnNow = returnNow;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (!args.hasNext() && this.returnNow) {
			return LocalDateTime.now();
		}

		CommandArgs.Snapshot state = args.getSnapshot();
		String date = args.next();

		try {
			return LocalDateTime.parse(date);
		} catch (DateTimeParseException ex) {
			try {
				return LocalDateTime.of(LocalDate.now(), LocalTime.parse(date));
			} catch (DateTimeParseException ex2) {
				try {
					return LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT);
				} catch (DateTimeParseException ex3) {
					if (this.returnNow) {
						args.applySnapshot(state);
						return LocalDateTime.now();
					}

					throw args.createError(Text.literal("Invalid date-time!"));
				}
			}
		}
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		String date = LocalDateTime.now().withNano(0).toString();

		if (date.startsWith(args.nextIfPresent().orElse(""))) {
			return ImmutableList.of(date);
		} else {
			return ImmutableList.of();
		}
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		if (!this.returnNow) {
			return super.getUsage(src);
		} else {
			return Text.literal("[" + this.getKey().toString() + "]");
		}
	}
}
