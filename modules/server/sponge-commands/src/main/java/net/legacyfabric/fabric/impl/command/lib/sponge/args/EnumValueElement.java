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

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.PatternMatchingCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumValueElement<T extends Enum<T>> extends PatternMatchingCommandElement {
	private final Class<T> type;
	private final Map<String, T> values;

	public EnumValueElement(Text key, Class<T> type) {
		super(key);
		this.type = type;
		this.values = Arrays.stream(type.getEnumConstants())
				.collect(Collectors.toMap(value -> value.name().toLowerCase(),
						Function.identity(), (value, value2) -> {
							throw new UnsupportedOperationException(type.getCanonicalName() + " contains more than one enum constant " + "with the same name, only differing by capitalization, which is unsupported.");
						}
				));
	}

	@Override
	protected Iterable<String> getChoices(PermissibleCommandSource source) {
		return this.values.keySet();
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		T value = this.values.get(choice.toLowerCase());

		if (value == null) {
			throw new IllegalArgumentException("No enum constant " + this.type.getCanonicalName() + "." + choice);
		}

		return value;
	}
}
