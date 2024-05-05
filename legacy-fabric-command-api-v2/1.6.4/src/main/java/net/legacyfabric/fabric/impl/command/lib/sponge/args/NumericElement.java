/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.KeyElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class NumericElement<T extends Number> extends KeyElement {
	private final Function<String, T> parseFunc;
	@Nullable
	private final BiFunction<String, Integer, T> parseRadixFunction;
	private final Function<String, ChatMessage> errorSupplier;

	public NumericElement(ChatMessage key, Function<String, T> parseFunc, @Nullable BiFunction<String, Integer, T> parseRadixFunction, Function<String, ChatMessage> errorSupplier) {
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
