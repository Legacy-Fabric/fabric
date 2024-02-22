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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

/**
 * Abstract command element that matches values based on a regex pattern.
 */
public abstract class PatternMatchingCommandElement extends CommandElement {
	private static final Text nullKeyArg = new LiteralText("argument");
	final boolean useRegex;

	/**
	 * @param yesIWantRegex Specify if you want to allow regex for users of
	 *                      the command element. Note that this will open up for DoS attacks.
	 */
	protected PatternMatchingCommandElement(@Nullable Text key, boolean yesIWantRegex) {
		super(key);
		this.useRegex = yesIWantRegex;
	}

	protected PatternMatchingCommandElement(@Nullable Text key) {
		this(key, false);
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		Iterable<String> choices = this.getChoices(source);
		Iterable<Object> ret;
		String arg = args.next();

		// Check to see if we have an exact match first
		Optional<Object> exactMatch = this.getExactMatch(choices, arg);

		if (exactMatch.isPresent()) {
			// Return this as a collection as this can get transformed by the subclass.
			return Collections.singleton(exactMatch.get());
		}

		if (this.useRegex) {
			Pattern pattern = this.getFormattedPattern(arg);
			ret = StreamSupport.stream(choices.spliterator(), false).filter(element -> pattern.matcher(element).find()).collect(Collectors.toList()).stream().map(this::getValue).collect(Collectors.toList());
		} else {
			Iterable<String> startsWith = StreamSupport.stream(choices.spliterator(), false).filter(element -> element.regionMatches(true, 0, arg, 0, arg.length())).collect(Collectors.toList());
			ret = StreamSupport.stream(startsWith.spliterator(), false).map(this::getValue).collect(Collectors.toList());
		}

		if (!ret.iterator().hasNext()) {
			throw args.createError(new LiteralText(String.format("No values matching pattern '%s' present for %s!", arg, this.getKey() == null
					? nullKeyArg : this.getKey())));
		}

		return ret;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		Iterable<String> choices = this.getChoices(src);
		final Optional<String> nextArg = args.nextIfPresent();

		if (nextArg.isPresent()) {
			if (this.useRegex) {
				choices = StreamSupport.stream(choices.spliterator(), false).filter(input -> this.getFormattedPattern(nextArg.get()).matcher(input).find()).collect(Collectors.toList());
			} else {
				String arg = nextArg.get();
				choices = StreamSupport.stream(choices.spliterator(), false).filter(input -> input.regionMatches(true, 0, arg, 0, arg.length())).collect(Collectors.toList());
			}
		}

		return ImmutableList.copyOf(choices);
	}

	Pattern getFormattedPattern(String input) {
		if (!input.startsWith("^")) { // Anchor matches to the beginning -- this lets us use find()
			input = "^" + input;
		}

		return Pattern.compile(input, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * Tests a string against a set of valid choices to see if it is a
	 * case-insensitive match.
	 *
	 * @param choices         The choices available to match against
	 * @param potentialChoice The potential choice
	 * @return If matched, an {@link Optional} containing the matched value
	 */
	protected Optional<Object> getExactMatch(final Iterable<String> choices, final String potentialChoice) {
		return Optional.ofNullable(Iterables.tryFind(choices, potentialChoice::equalsIgnoreCase).orNull()).map(this::getValue);
	}

	/**
	 * Gets the available choices for this command source.
	 *
	 * @param source The source requesting choices
	 * @return the possible choices
	 */
	protected abstract Iterable<String> getChoices(PermissibleCommandSource source);

	/**
	 * Gets the value for a given choice. For any result in
	 * {@link #getChoices(PermissibleCommandSource)}, this must return a non-null value.
	 * Otherwise, an {@link IllegalArgumentException} may be throw.
	 *
	 * @param choice The specified choice
	 * @return the choice's value
	 * @throws IllegalArgumentException if the input string is not any return
	 *                                  value of {@link #getChoices(PermissibleCommandSource)}
	 */
	protected abstract Object getValue(String choice) throws IllegalArgumentException;
}
