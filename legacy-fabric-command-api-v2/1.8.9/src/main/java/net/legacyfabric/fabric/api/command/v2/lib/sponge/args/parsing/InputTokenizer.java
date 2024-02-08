/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing;

import java.util.List;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;

public interface InputTokenizer {
	/**
	 * Use an input string tokenizer that supports quoted arguments and
	 * character escapes.
	 *
	 * <p>Forcing lenient to true makes the following apply:</p>
	 *
	 * <ul>
	 *     <li>Unclosed quotations are treated as a single string from the
	 *     opening quotation to the end of the arguments rather than throwing
	 *     an exception </li>
	 * </ul>
	 *
	 * @param forceLenient Whether the tokenizer is forced into lenient mode
	 * @return the appropriate tokenizer
	 */
	static InputTokenizer quotedStrings(boolean forceLenient) {
		return new QuotedStringTokenizer(true, forceLenient, false);
	}

	/**
	 * Returns an input tokenizer that takes input strings and splits them by
	 * space.
	 *
	 * @return The appropriate tokenizer
	 */
	static InputTokenizer spaceSplitString() {
		return SpaceSplitInputTokenizer.INSTANCE;
	}

	/**
	 * Returns an input tokenizer that returns the input string as a single
	 * argument.
	 *
	 * @return The appropriate tokenizer
	 */
	static InputTokenizer rawInput() {
		return RawStringInputTokenizer.INSTANCE;
	}

	/**
	 * Take the input string and split it as appropriate into argument tokens.
	 *
	 * @param arguments The provided arguments
	 * @param lenient   Whether to parse leniently
	 * @return The tokenized strings. Empty list if error occurs
	 * @throws ArgumentParseException if an invalid input is provided
	 */
	List<SingleArg> tokenize(String arguments, boolean lenient) throws ArgumentParseException;
}
