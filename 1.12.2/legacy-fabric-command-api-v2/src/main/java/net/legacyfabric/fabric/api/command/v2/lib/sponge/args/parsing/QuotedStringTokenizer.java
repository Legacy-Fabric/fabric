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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.text.LiteralText;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;

/**
 * Parser for converting a quoted string into a list of arguments.
 *
 * <p>Grammar is roughly (yeah, this is not really a proper grammar but it gives
 * you an idea of what's happening:</p>
 *
 * <blockquote><pre> WHITESPACE = Character.isWhiteSpace(codePoint)
 * CHAR := (all unicode)
 * ESCAPE := '\' CHAR
 * QUOTE = ' | "
 * UNQUOTED_ARG := (CHAR | ESCAPE)+ WHITESPACE
 * QUOTED_ARG := QUOTE (CHAR | ESCAPE)+ QUOTE
 * ARGS := ((UNQUOTED_ARG | QUOTED_ARG) WHITESPACE+)+</pre></blockquote>
 */
class QuotedStringTokenizer implements InputTokenizer {
	private static final int CHAR_BACKSLASH = '\\';
	private static final int CHAR_SINGLE_QUOTE = '\'';
	private static final int CHAR_DOUBLE_QUOTE = '"';
	private final boolean handleQuotedStrings;
	private final boolean forceLenient;
	private final boolean trimTrailingSpace;

	QuotedStringTokenizer(boolean handleQuotedStrings, boolean forceLenient, boolean trimTrailingSpace) {
		this.handleQuotedStrings = handleQuotedStrings;
		this.forceLenient = forceLenient;
		this.trimTrailingSpace = trimTrailingSpace;
	}

	@Override
	public List<SingleArg> tokenize(String arguments, boolean lenient) throws ArgumentParseException {
		if (arguments.length() == 0) {
			return Collections.emptyList();
		}

		final TokenizerState state = new TokenizerState(arguments, lenient);
		List<SingleArg> returnedArgs = new ArrayList<>(arguments.length() / 4);

		if (this.trimTrailingSpace) {
			this.skipWhiteSpace(state);
		}

		while (state.hasMore()) {
			if (!this.trimTrailingSpace) {
				this.skipWhiteSpace(state);
			}

			int startIdx = state.getIndex() + 1;
			String arg = this.nextArg(state);
			returnedArgs.add(new SingleArg(arg, startIdx, state.getIndex()));

			if (this.trimTrailingSpace) {
				this.skipWhiteSpace(state);
			}
		}

		return returnedArgs;
	}

	// Parsing methods

	private void skipWhiteSpace(TokenizerState state) throws ArgumentParseException {
		if (!state.hasMore()) {
			return;
		}

		while (state.hasMore() && Character.isWhitespace(state.peek())) {
			state.next();
		}
	}

	private String nextArg(TokenizerState state) throws ArgumentParseException {
		StringBuilder argBuilder = new StringBuilder();

		if (state.hasMore()) {
			int codePoint = state.peek();

			if (this.handleQuotedStrings && (codePoint == CHAR_DOUBLE_QUOTE || codePoint == CHAR_SINGLE_QUOTE)) {
				// quoted string
				this.parseQuotedString(state, codePoint, argBuilder);
			} else {
				this.parseUnquotedString(state, argBuilder);
			}
		}

		return argBuilder.toString();
	}

	private void parseQuotedString(TokenizerState state, int startQuotation, StringBuilder builder) throws ArgumentParseException {
		// Consume the start quotation character
		int nextCodePoint = state.next();

		if (nextCodePoint != startQuotation) {
			throw state.createException(new LiteralText(String.format("Actual next character '%c' did not match expected quotation character '%c'",
					nextCodePoint, startQuotation)));
		}

		while (true) {
			if (!state.hasMore()) {
				if (state.isLenient() || this.forceLenient) {
					return;
				}

				throw state.createException(new LiteralText("Unterminated quoted string found"));
			}

			nextCodePoint = state.peek();

			if (nextCodePoint == startQuotation) {
				state.next();
				return;
			} else if (nextCodePoint == CHAR_BACKSLASH) {
				this.parseEscape(state, builder);
			} else {
				builder.appendCodePoint(state.next());
			}
		}
	}

	private void parseUnquotedString(TokenizerState state, StringBuilder builder) throws ArgumentParseException {
		while (state.hasMore()) {
			int nextCodePoint = state.peek();

			if (Character.isWhitespace(nextCodePoint)) {
				return;
			} else if (nextCodePoint == CHAR_BACKSLASH) {
				this.parseEscape(state, builder);
			} else {
				builder.appendCodePoint(state.next());
			}
		}
	}

	private void parseEscape(TokenizerState state, StringBuilder builder) throws ArgumentParseException {
		state.next(); // Consume \
		builder.appendCodePoint(state.next()); // TODO: Unicode character escapes (\u00A7 type thing)?
	}
}
