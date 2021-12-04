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

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nullable;

import io.github.legacyrewoven.api.command.v2.lib.sponge.args.ArgumentParseException;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandArgs;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.KeyElement;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.Text;

public class NumericElement<T extends Number> extends KeyElement {
	private final Function<String, T> parseFunc;
	@Nullable
	private final BiFunction<String, Integer, T> parseRadixFunction;
	private final Function<String, Text> errorSupplier;

	public NumericElement(Text key, Function<String, T> parseFunc, @Nullable BiFunction<String, Integer, T> parseRadixFunction, Function<String, Text> errorSupplier) {
		super(key);
		this.parseFunc = parseFunc;
		this.parseRadixFunction = parseRadixFunction;
		this.errorSupplier = errorSupplier;
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		final String input = args.next();

		try {
			if (this.parseRadixFunction != null) {
				if (input.startsWith("0x")) {
					return this.parseRadixFunction.apply(input.substring(2), 16);
				} else if (input.startsWith("0b")) {
					return this.parseRadixFunction.apply(input.substring(2), 2);
				}
			}

			return this.parseFunc.apply(input);
		} catch (NumberFormatException ex) {
			throw args.createError(this.errorSupplier.apply(input));
		}
	}
}
