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

package io.github.legacyrewoven.api.command.v2.lib.sponge.args;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.legacyrewoven.api.command.v2.StringType;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;
import io.github.legacyrewoven.api.util.TriState;
import io.github.legacyrewoven.impl.command.lib.sponge.args.AllOfCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.BigDecimalElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.BigIntegerElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.ChoicesCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.DateTimeElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.DurationElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.EntityCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.EnumValueElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.FilteredSuggestionsElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.FirstParsingCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.IpElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.LiteralCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.MarkTrueCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.ModCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.NumericElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.OnlyOneCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.OptionalCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.PermissionCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.PlayerCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.RemainingJoinedStringsCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.RepeatedCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.SequenceCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.StringCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.StringElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.UrlElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.UuidElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.Vec3dCommandElement;
import io.github.legacyrewoven.impl.command.lib.sponge.args.WithSuggestionsElement;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.loader.api.ModContainer;

/**
 * Class containing factory methods for common command elements.
 */
@SuppressWarnings({"UnstableApiUsage"})
public class GenericArguments {
	private static final CommandElement NONE = new SequenceCommandElement(ImmutableList.of());

	private GenericArguments() {
	}

	/**
	 * Expects no arguments, returns no values.
	 *
	 * @return An expectation of no arguments
	 */
	public static CommandElement none() {
		return NONE;
	}

	/**
	 * Expects no arguments. Adds 'true' to the context when parsed.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key the key to store 'true' under
	 * @return the argument
	 */
	public static CommandElement markTrue(Text key) {
		return new MarkTrueCommandElement(key);
	}

	/**
	 * Expect an argument to represent online players, or if nothing matches
	 * and the source is a {@link PlayerEntity}, give the player. If nothing matches
	 * and the source is not a player, throw an exception.
	 *
	 * <p>Gives values of type {@link PlayerEntity}.</p>
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>A player's name</li>
	 *     <li>A regex that matches the beginning of one or more player's names
	 *     </li>
	 *     <li>A selector</li>
	 * </ul>
	 *
	 * <p>This may return multiple players. If you must only return one, wrap
	 * this element in an {@link #onlyOne(CommandElement)} call.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement playerOrSource(Text key) {
		return new PlayerCommandElement(key, true);
	}

	/**
	 * Expect an argument to represent online players. Returns values of type
	 * {@link PlayerEntity}.
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>A player's name</li>
	 *     <li>A regex that matches the beginning of one or more player's names
	 *     </li>
	 *     <li>A selector</li>
	 * </ul>
	 *
	 * <p>This may return multiple players. If you must only return one, wrap
	 * this element in an {@link #onlyOne(CommandElement)} call.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement player(Text key) {
		return new PlayerCommandElement(key, false);
	}

	/**
	 * Expect an argument to represent a {@link Vec3d}.
	 *
	 * <p>This will return one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement vec3d(Text key) {
		return new Vec3dCommandElement(key);
	}

	/**
	 * Expect an argument to represent a {@link ModContainer}'s id.
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>The specified {@link ModContainer}'s id</li>
	 *     <li>A regex that matches the beginning of one or more plugin id</li>
	 * </ul>
	 *
	 * <p>This may return multiple {@link ModContainer}s. If you must only
	 * return one, wrap this element in an {@link #onlyOne(CommandElement)}
	 * call.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement mod(Text key) {
		return new ModCommandElement(key);
	}

	/**
	 * Gets a builder to create a command element that parses flags.
	 *
	 * <p>There should only be ONE of these in a command element sequence if you
	 * wish to use flags. A {@link CommandFlags.Builder} can handle multiple
	 * flags that have different behaviors. Using multiple builders in the same
	 * sequence may cause unexpected behavior.</p>
	 *
	 * <p>Any command elements that are not associated with flags should be
	 * placed into the {@link CommandFlags.Builder#buildWith(CommandElement)}
	 * parameter, allowing the flags to be used throughout the argument string.
	 * </p>
	 *
	 * @return the newly created builder
	 */
	public static CommandFlags.Builder flags() {
		return new CommandFlags.Builder();
	}

	/**
	 * Consumes a series of arguments. Usage is the elements concatenated
	 *
	 * @param elements The series of arguments to expect
	 * @return the element to match the input
	 */
	public static CommandElement seq(CommandElement... elements) {
		return new SequenceCommandElement(ImmutableList.copyOf(elements));
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 *
	 * <p>If there are 5 or fewer choices available, the choices will be shown
	 * in the command usage. Otherwise, the usage will only display only the
	 * key.</p>
	 *
	 * <p>Choices are <strong>case sensitive</strong>. If you do not require
	 * case sensitivity, see {@link #choicesInsensitive(Text, Map)}.</p>
	 *
	 * <p>To override this behavior, see
	 * {@link #choices(Text, Map, boolean, boolean)}.</p>
	 *
	 * <p>When parsing, only one choice may be selected, returning its
	 * associated value.</p>
	 *
	 * @param key     The key to store the resulting value under
	 * @param choices The choices users can choose from
	 * @return the element to match the input
	 */
	public static CommandElement choices(Text key, Map<String, ?> choices) {
		return choices(key, choices, choices.size() <= ChoicesCommandElement.CUTOFF, true);
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 *
	 * <p>If there are 5 or fewer choices available, the choices will be shown
	 * in the command usage. Otherwise, the usage will only display only the
	 * key.</p>
	 *
	 * <p>Choices are <strong>not case sensitive</strong>. If you require
	 * case sensitivity, see {@link #choices(Text, Map)}</p>
	 *
	 * <p>To override this behavior, see
	 * {@link #choices(Text, Map, boolean, boolean)}.</p>
	 *
	 * <p>When parsing, only one choice may be selected, returning its
	 * associated value.</p>
	 *
	 * @param key     The key to store the resulting value under
	 * @param choices The choices users can choose from
	 * @return the element to match the input
	 */
	public static CommandElement choicesInsensitive(Text key, Map<String, ?> choices) {
		return choices(key, choices, choices.size() <= ChoicesCommandElement.CUTOFF, false);
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 *
	 * <p>Unless {@code choicesInUsage} is true, general command usage will only
	 * display the provided key.</p>
	 *
	 * <p>Choices are <strong>case sensitive</strong>. If you do not require
	 * case sensitivity, see {@link #choices(Text, Map, boolean, boolean)}</p>
	 *
	 * <p>When parsing, only one choice may be selected, returning its
	 * associated value.</p>
	 *
	 * @param key            The key to store the resulting value under
	 * @param choices        The choices users can choose from
	 * @param choicesInUsage Whether to display the available choices, or simply
	 *                       the provided key, as part of usage
	 * @return the element to match the input
	 */
	public static CommandElement choices(Text key, Map<String, ?> choices, boolean choicesInUsage) {
		return choices(key, choices, choicesInUsage, true);
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 *
	 * <p>Unless {@code choicesInUsage} is true, general command usage will only
	 * display the provided key.</p>
	 *
	 * <p>When parsing, only one choice may be selected, returning its
	 * associated value.</p>
	 *
	 * @param key            The key to store the resulting value under
	 * @param choices        The choices users can choose from
	 * @param choicesInUsage Whether to display the available choices, or simply
	 *                       the provided key, as part of usage
	 * @param caseSensitive  Whether the matches should be case sensitive
	 * @return the element to match the input
	 */
	public static CommandElement choices(Text key, Map<String, ?> choices, boolean choicesInUsage, boolean caseSensitive) {
		if (!caseSensitive) {
			Map<String, Object> immChoices = choices.entrySet().stream().collect(ImmutableMap.toImmutableMap(x -> x.getKey().toLowerCase(), Map.Entry::getValue));
			return choices(key, immChoices::keySet, selection -> immChoices.get(selection.toLowerCase()), choicesInUsage);
		}

		Map<String, Object> immChoices = ImmutableMap.copyOf(choices);
		return choices(key, immChoices::keySet, immChoices::get, choicesInUsage);
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 *
	 * <p>If there are 5 or fewer choices available, the choices will be shown
	 * in the command usage. Otherwise, the usage will only display only the
	 * key.</p>
	 *
	 * <p>To override this behavior, see {@link #choices(Text, Map, boolean)}.
	 * </p>
	 *
	 * <p>Only one choice may be selected, returning its associated value.</p>
	 *
	 * @param key    The key to store the resulting value under
	 * @param keys   The function that will supply available keys
	 * @param values The function that maps an element of {@code key} to a value
	 *               and any other key to {@code null}
	 * @return the element to match the input
	 */
	public static CommandElement choices(Text key, Supplier<Collection<String>> keys, Function<String, ?> values) {
		return new ChoicesCommandElement(key, keys, values, TriState.DEFAULT);
	}

	/**
	 * Return an argument that allows selecting from a limited set of values.
	 * Unless {@code choicesInUsage} is true, general command usage will only
	 * display the provided key.
	 *
	 * <p>Only one choice may be selected, returning its associated value.</p>
	 *
	 * @param key            The key to store the resulting value under
	 * @param keys           The function that will supply available keys
	 * @param values         The function that maps an element of {@code key} to a value
	 *                       and any other key to {@code null}
	 * @param choicesInUsage Whether to display the available choices, or simply
	 *                       the provided key, as part of usage
	 * @return the element to match the input
	 */
	public static CommandElement choices(Text key, Supplier<Collection<String>> keys, Function<String, ?> values, boolean choicesInUsage) {
		return new ChoicesCommandElement(key, keys, values, choicesInUsage ? TriState.TRUE : TriState.FALSE);
	}

	/**
	 * Returns a command element that matches the first of the provided elements
	 * that parses tab completion matches from all options.
	 *
	 * @param elements The elements to check against
	 * @return The command element matching the first passing of the elements
	 * provided
	 */
	public static CommandElement firstParsing(CommandElement... elements) {
		return new FirstParsingCommandElement(ImmutableList.copyOf(elements));
	}

	/**
	 * Make the provided command element optional.
	 *
	 * <p>This means the command element is not required. However, if the
	 * element is provided with invalid format and there are no more args
	 * specified, any errors will still be passed on.</p>
	 *
	 * @param element The element to optionally require
	 * @return the element to match the input
	 */
	public static CommandElement optional(CommandElement element) {
		return new OptionalCommandElement(element, null, false);
	}

	/**
	 * Make the provided command element optional.
	 *
	 * <p>This means the command element is not required. However, if the
	 * element is provided with invalid format and there are no more args
	 * specified, any errors will still be passed on. If the given element's key
	 * and {@code value} are not null and this element is not provided the
	 * element's key will be set to the given value.</p>
	 *
	 * @param element The element to optionally require
	 * @param value   The default value to set
	 * @return the element to match the input
	 */
	public static CommandElement optional(CommandElement element, Object value) {
		return new OptionalCommandElement(element, value, false);
	}

	/**
	 * Make the provided command element optional
	 * This means the command element is not required.
	 * If the argument is provided but of invalid format, it will be skipped.
	 *
	 * @param element The element to optionally require
	 * @return the element to match the input
	 */
	public static CommandElement optionalWeak(CommandElement element) {
		return new OptionalCommandElement(element, null, true);
	}

	/**
	 * <p>Make the provided command element optional.</p>
	 *
	 * <p>This means the command element is not required.</p>
	 *
	 * <ul>
	 *     <li>If the argument is provided but of invalid format, it will be
	 *     skipped.</li>
	 *     <li>If the given element's key and {@code value} are not null and
	 *     this element is not provided the element's key will be set to the
	 *     given value.</li>
	 * </ul>
	 *
	 * @param element The element to optionally require
	 * @param value   The default value to set
	 * @return the element to match the input
	 */
	public static CommandElement optionalWeak(CommandElement element, Object value) {
		return new OptionalCommandElement(element, value, true);
	}

	/**
	 * Require a given command element to be provided a certain number of times.
	 *
	 * <p>Command values will be stored under their provided keys in the
	 * <tt>CommandContext</tt>.</p>
	 *
	 * @param element The element to repeat
	 * @param times   The number of times to repeat the element.
	 * @return the element to match the input
	 */
	public static CommandElement repeated(CommandElement element, int times) {
		return new RepeatedCommandElement(element, times);
	}

	/**
	 * Require all remaining args to match as many instances of
	 * {@link CommandElement} as will fit. Command element values will be stored
	 * under their provided keys in the CommandContext.
	 *
	 * @param element The element to repeat
	 * @return the element to match the input
	 */
	public static CommandElement allOf(CommandElement element) {
		return new AllOfCommandElement(element);
	}

	// -- Argument types for basic java types

	/**
	 * Require an argument to be a string. Any provided argument will fit in
	 * under this argument.
	 *
	 * <p>Gives values of type {@link String}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement string(Text key) {
		return new StringElement(key);
	}

	/**
	 * Require an argument to be an integer (base 10).
	 *
	 * <p>Gives values of type {@link Integer}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement integer(Text key) {
		return new NumericElement<>(key, Integer::parseInt, Integer::parseInt, input -> new LiteralText(String.format("Expected an integer, but input '%s' was not", input)));
	}

	/**
	 * Require an argument to be a long (base 10).
	 *
	 * <p>Gives values of type {@link Long}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement longNum(Text key) {
		return new NumericElement<>(key, Long::parseLong, Long::parseLong, input -> new LiteralText(String.format("Expected a long, but input '%s' was not", input)));
	}

	/**
	 * Require an argument to be an double-precision floating point number.
	 *
	 * <p>Gives values of type {@link Double}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement doubleNum(Text key) {
		return new NumericElement<>(key, Double::parseDouble, null, input -> new LiteralText(String.format("Expected a number, but input '%s' was not", input)));
	}

	private static final Map<String, Boolean> BOOLEAN_CHOICES = ImmutableMap.<String, Boolean>builder()
			.put("true", true)
			.put("t", true)
			.put("y", true)
			.put("yes", true)
			.put("verymuchso", true)
			.put("1", true)
			.put("false", false)
			.put("f", false)
			.put("n", false)
			.put("no", false)
			.put("notatall", false)
			.put("0", false)
			.build();

	/**
	 * Require an argument to be a boolean.
	 *
	 * <p>The recognized true values are:</p>
	 *
	 * <ul>
	 *     <li>true</li>
	 *     <li>t</li>
	 *     <li>yes</li>
	 *     <li>y</li>
	 *     <li>verymuchso</li>
	 * </ul>
	 *
	 *
	 * <p>The recognized false values are:</p>
	 *
	 * <ul>
	 *     <li>false</li>
	 *     <li>f</li>
	 *     <li>no</li>
	 *     <li>n</li>
	 *     <li>notatall</li>
	 * </ul>
	 *
	 * <p>Gives values of type {@link Boolean}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement bool(Text key) {
		return GenericArguments.choices(key, BOOLEAN_CHOICES);
	}

	/**
	 * Require the argument to be a key under the provided enum.
	 *
	 * <p>Gives values of type <tt>T</tt>. This will return only one value.</p>
	 *
	 * @param key  The key to store the matched enum value under
	 * @param type The enum class to get enum constants from
	 * @param <T>  The type of enum
	 * @return the element to match the input
	 */
	public static <T extends Enum<T>> CommandElement enumValue(Text key, Class<T> type) {
		return new EnumValueElement<>(key, type);
	}

	/**
	 * Require one or more strings, which are combined into a single,
	 * space-separated string.
	 *
	 * <p>Gives values of type {@link String}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement remainingJoinedStrings(Text key) {
		return new RemainingJoinedStringsCommandElement(key, false);
	}

	/**
	 * Require one or more strings, without any processing, which are combined
	 * into a single, space-separated string.
	 *
	 * <p>Gives values of type {@link String}. This will return only one value.
	 * </p>
	 *
	 * @param key The key to store the parsed argument under
	 * @return the element to match the input
	 */
	public static CommandElement remainingRawJoinedStrings(Text key) {
		return new RemainingJoinedStringsCommandElement(key, true);
	}

	/**
	 * Expect a literal sequence of arguments. This element matches the input
	 * against a predefined array of arguments expected to be present,
	 * case-insensitively.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key          The key to add to the context. Will be set to a value of true
	 *                     if this element matches
	 * @param expectedArgs The sequence of arguments expected
	 * @return the appropriate command element
	 */
	public static CommandElement literal(Text key, String... expectedArgs) {
		return new LiteralCommandElement(key, ImmutableList.copyOf(expectedArgs), true);
	}

	/**
	 * Expect a literal sequence of arguments. This element matches the input
	 * against a predefined array of arguments expected to be present,
	 * case-insensitively.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key          The key to store this argument as
	 * @param putValue     The value to put at key if this argument matches. May be
	 *                     <tt>null</tt>
	 * @param expectedArgs The sequence of arguments expected
	 * @return the appropriate command element
	 */
	public static CommandElement literal(Text key, @Nullable Object putValue, String... expectedArgs) {
		return new LiteralCommandElement(key, ImmutableList.copyOf(expectedArgs), putValue);
	}

	/**
	 * Restricts the given command element to only insert one value into the
	 * context at the provided key.
	 *
	 * <p>If more than one value is returned by an element, or the target key
	 * already contains a value, this will throw an
	 * {@link ArgumentParseException}</p>
	 *
	 * @param element The element to restrict
	 * @return the restricted element
	 */
	public static CommandElement onlyOne(CommandElement element) {
		return new OnlyOneCommandElement(element);
	}

	/**
	 * Checks a permission for a given command argument to be used.
	 *
	 * <p>If the element attempts to parse an argument and the user does not
	 * have the permission, an {@link ArgumentParseException} will be thrown.</p>
	 *
	 * @param element    The element to wrap
	 * @param permission The permission to check
	 * @return the element
	 */
	public static CommandElement requiringPermission(CommandElement element, String permission) {
		return new PermissionCommandElement(element, permission, false);
	}

	/**
	 * Checks a permission for a given command argument to be used.
	 *
	 * <p>If the element attempts to parse an argument and the user does not
	 * have the permission, the element will be skipped over.</p>
	 *
	 * <p>If the invoking {@link PermissibleCommandSource} has permission to use the
	 * element, but the wrapped element fails to parse, an exception will
	 * be reported in the normal way. If you require this element to be
	 * truly optional, wrap this element in either
	 * {@link #optional(CommandElement)} or
	 * {@link #optionalWeak(CommandElement)}, as required.</p>
	 *
	 * @param element    The element to wrap
	 * @param permission The permission to check
	 * @return the element
	 */
	public static CommandElement requiringPermissionWeak(CommandElement element, String permission) {
		return new PermissionCommandElement(element, permission, true);
	}

	/**
	 * Expect an argument to represent an {@link Entity}.
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>A player's name</li>
	 *     <li>An entity's {@link UUID}</li>
	 *     <li>A regex that matches the beginning of one or more player's names
	 *     or entities UUIDs.
	 *     </li>
	 *     <li>A selector</li>
	 * </ul>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement entity(Text key) {
		return new EntityCommandElement(key, false, false, null);
	}

	/**
	 * Expect an argument to represent an {@link Entity} of the specified type.
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>A player's name (if appropriate)</li>
	 *     <li>An entity's {@link UUID}</li>
	 *     <li>A regex that matches the beginning of one or more player's names
	 *     or entities UUIDs.
	 *     </li>
	 *     <li>A selector</li>
	 * </ul>
	 *
	 * @param key   The key to store under
	 * @param clazz The type which the entity must subclass
	 * @return the argument
	 */
	public static CommandElement entity(Text key, Class<? extends Entity> clazz) {
		return new EntityCommandElement(key, false, false, clazz);
	}

	/**
	 * Expect an argument to represent an {@link Entity} of the specified type,
	 * or if the argument is not present and the {@link PermissibleCommandSource} is
	 * looking at an applicable entity, return that entity.
	 *
	 * <p>This argument accepts the following inputs:</p>
	 *
	 * <ul>
	 *     <li>A player's name (if appropriate)</li>
	 *     <li>An entity's {@link UUID}</li>
	 *     <li>A regex that matches the beginning of one or more player's names
	 *     or entities UUIDs.
	 *     </li>
	 *     <li>A selector</li>
	 * </ul>
	 *
	 * @param key   The key to store under
	 * @param clazz The type which the entity must subclass
	 * @return the argument
	 */
	public static CommandElement entityOrTarget(Text key, Class<? extends Entity> clazz) {
		return new EntityCommandElement(key, false, true, clazz);
	}

	/**
	 * Expect an argument to represent a {@link URL}.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement url(Text key) {
		return new UrlElement(key);
	}

	/**
	 * Expect an argument to return an IP address, in the form of an
	 * {@link InetAddress}.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement ip(Text key) {
		return new IpElement(key);
	}

	/**
	 * Expect an argument to return a {@link BigDecimal}.
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement bigDecimal(Text key) {
		return new BigDecimalElement(key);
	}

	/**
	 * Expect an argument to return a {@link BigInteger}.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement bigInteger(Text key) {
		return new BigIntegerElement(key);
	}

	/**
	 * Expect an argument to be a {@link UUID}.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement uuid(Text key) {
		return new UuidElement(key);
	}

	/**
	 * Expect an argument to be a {@link String}.
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key  The key to store under
	 * @param type The type of parsing to be followed
	 * @return the argument
	 */
	public static CommandElement string(Text key, StringType type) {
		return new StringCommandElement(key, type);
	}

	/**
	 * Expect an argument to be a date-time, in the form of a
	 * {@link LocalDateTime}. If no date is specified, {@link LocalDate#now()}
	 * is used; if no time is specified, {@link LocalTime#MIDNIGHT} is used.
	 *
	 * <p>Date-times are expected in the ISO-8601 format.</p>
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
	 */
	public static CommandElement dateTime(Text key) {
		return new DateTimeElement(key, false);
	}

	/**
	 * Expect an argument to be a date-time, in the form of a
	 * {@link LocalDateTime}. If no date is specified, {@link LocalDate#now()}
	 * is used; if no time is specified, {@link LocalTime#MIDNIGHT} is used.
	 *
	 * <p>If no argument at all is specified, defaults to
	 * {@link LocalDateTime#now()}.</p>
	 *
	 * <p>Date-times are expected in the ISO-8601 format.</p>
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement dateTimeOrNow(Text key) {
		return new DateTimeElement(key, true);
	}

	/**
	 * Expect an argument to be a {@link Duration}.
	 *
	 * <p>Durations are expected in the following format: {@code #D#H#M#S}.
	 * This is not case sensitive.</p>
	 *
	 * <p>This will return only one value.</p>
	 *
	 * @param key The key to store under
	 * @return the argument
	 */
	public static CommandElement duration(Text key) {
		return new DurationElement(key);
	}

	/**
	 * Uses a custom set of suggestions for an argument. The provided
	 * suggestions will replace the regular ones.
	 *
	 * @param argument    The element to replace the suggestions of
	 * @param suggestions The suggestions to use
	 * @return the argument
	 */
	public static CommandElement withSuggestions(CommandElement argument, Iterable<String> suggestions) {
		return withSuggestions(argument, suggestions, true);
	}

	/**
	 * Uses a custom set of suggestions for an argument. The provided
	 * suggestions will replace the regular ones.
	 *
	 * <p>If {@code requireBegin} is false, then the already typed argument
	 * will not be used to filter the provided suggestions.</p>
	 *
	 * @param argument     The element to replace the suggestions of
	 * @param suggestions  The suggestions to use
	 * @param requireBegin Whether or not to require the current argument to
	 *                     begin provided arguments
	 * @return the argument
	 */
	public static CommandElement withSuggestions(CommandElement argument, Iterable<String> suggestions, boolean requireBegin) {
		return withSuggestions(argument, (s) -> suggestions, requireBegin);
	}

	/**
	 * Uses a custom set of suggestions for an argument. The provided
	 * suggestions will replace the regular ones.
	 *
	 * @param argument    The element to replace the suggestions of
	 * @param suggestions A function to return the suggestions to use
	 * @return the argument
	 */
	public static CommandElement withSuggestions(CommandElement argument, Function<PermissibleCommandSource, Iterable<String>> suggestions) {
		return withSuggestions(argument, suggestions, true);
	}

	/**
	 * Uses a custom set of suggestions for an argument. The provided
	 * suggestions will replace the regular ones.
	 *
	 * <p>If {@code requireBegin} is false, then the already typed argument
	 * will not be used to filter the provided suggestions.</p>
	 *
	 * @param argument     The element to replace the suggestions of
	 * @param suggestions  A function to return the suggestions to use
	 * @param requireBegin Whether or not to require the current argument to
	 *                     begin provided arguments
	 * @return the argument
	 */
	public static CommandElement withSuggestions(CommandElement argument, Function<PermissibleCommandSource,
			Iterable<String>> suggestions, boolean requireBegin) {
		return new WithSuggestionsElement(argument, suggestions, requireBegin);
	}

	/**
	 * Filters an argument's suggestions. A suggestion will only be used if it
	 * matches the predicate.
	 *
	 * @param argument  The element to filter the suggestions of
	 * @param predicate The predicate to test suggestions against
	 * @return the argument
	 */
	public static CommandElement withConstrainedSuggestions(CommandElement argument, Predicate<String> predicate) {
		return new FilteredSuggestionsElement(argument, predicate);
	}
}
