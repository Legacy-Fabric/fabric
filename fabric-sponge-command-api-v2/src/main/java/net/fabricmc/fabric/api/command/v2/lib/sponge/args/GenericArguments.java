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
package net.fabricmc.fabric.api.command.v2.lib.sponge.args;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;
import net.fabricmc.fabric.api.command.v2.StringType;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;


/**
 * Class containing factory methods for common command elements.
 */
@SuppressWarnings({"UnstableApiUsage", "ConstantConditions"})
public final class GenericArguments {
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

	private static class ModCommandElement extends PatternMatchingCommandElement {

		protected ModCommandElement(@Nullable Text key) {
			super(key);
		}

		@Override
		protected Iterable<String> getChoices(PermissibleCommandSource source) {
			return FabricLoader.getInstance().getAllMods().stream().map(container -> container.getMetadata().getId()).collect(Collectors.toSet());
		}

		@Override
		protected Object getValue(String choice) throws IllegalArgumentException {
			Optional<ModContainer> plugin = FabricLoader.getInstance().getModContainer(choice);
			return plugin.orElseThrow(() -> new IllegalArgumentException("Mod " + choice + " was not found"));
		}
	}

	static class MarkTrueCommandElement extends CommandElement {
		MarkTrueCommandElement(Text key) {
			super(key);
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return true;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			return Collections.emptyList();
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return new LiteralText("");
		}
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

	private static class SequenceCommandElement extends CommandElement {
		private final List<CommandElement> elements;

		SequenceCommandElement(List<CommandElement> elements) {
			super(null);
			this.elements = elements;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			for (CommandElement element : this.elements) {
				element.parse(source, args, context);
			}
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return null;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			Set<String> completions = Sets.newHashSet();
			for (CommandElement element : this.elements) {
				CommandArgs.Snapshot state = args.getSnapshot();
				CommandContext.Snapshot contextSnapshot = context.createSnapshot();
				try {
					element.parse(src, args, context);

					// If we get here, the parse occurred successfully.
					// However, if nothing was consumed, then we should consider
					// what could have been.
					CommandContext.Snapshot afterSnapshot = context.createSnapshot();
					if (state.equals(args.getSnapshot())) {
						context.applySnapshot(contextSnapshot);
						completions.addAll(element.complete(src, args, context));
						args.applySnapshot(state);
						context.applySnapshot(afterSnapshot);
					} else if (args.hasNext()) {
						completions.clear();
					} else {
						// What we might also have - we have no args left to parse so
						// while the parse itself was successful, there could be other
						// valid entries to add...
						context.applySnapshot(contextSnapshot);
						args.applySnapshot(state);
						completions.addAll(element.complete(src, args, context));
						if (!(element instanceof OptionalCommandElement)) {
							break;
						}

						// The last element was optional, so we go back to before this
						// element would have been parsed, and assume it never existed...
						context.applySnapshot(contextSnapshot);
						args.applySnapshot(state);
					}
				} catch (ArgumentParseException ignored) {
					args.applySnapshot(state);
					context.applySnapshot(contextSnapshot);
					completions.addAll(element.complete(src, args, context));
					break;
				}
			}
			return Lists.newArrayList(completions);
		}

		@Override
		public Text getUsage(PermissibleCommandSource commander) {
			final Text build = new LiteralText("");
			for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext(); ) {
				Text usage = it.next().getUsage(commander);
				if (!usage.getString().isEmpty()) {
					build.append(usage);
					if (it.hasNext()) {
						build.append(CommandMessageFormatting.SPACE_TEXT);
					}
				}
			}
			return new LiteralText(build.getString());
		}
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
			Map<String, Object> immChoices = choices.entrySet().stream()
					.collect(ImmutableMap.toImmutableMap(x -> x.getKey().toLowerCase(), Map.Entry::getValue));
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

	private static class ChoicesCommandElement extends CommandElement {
		private static final int CUTOFF = 5;
		private final Supplier<Collection<String>> keySupplier;
		private final Function<String, ?> valueSupplier;
		private final TriState choicesInUsage;

		ChoicesCommandElement(Text key, Supplier<Collection<String>> keySupplier, Function<String, ?> valueSupplier, TriState choicesInUsage) {
			super(key);
			this.keySupplier = keySupplier;
			this.valueSupplier = valueSupplier;
			this.choicesInUsage = choicesInUsage;
		}

		@Override
		public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			Object value = this.valueSupplier.apply(args.next());
			if (value == null) {
				throw args.createError(new LiteralText(String.format("Argument was not a valid choice. Valid choices: %s", this.keySupplier.get().toString())));
			}
			return value;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			final String prefix = args.nextIfPresent().orElse("");
			return this.keySupplier.get().stream().filter((input) -> input.startsWith(prefix)).collect(ImmutableList.toImmutableList());
		}

		@Override
		public Text getUsage(PermissibleCommandSource commander) {
			Collection<String> keys = this.keySupplier.get();
			if (this.choicesInUsage == TriState.TRUE || (this.choicesInUsage == TriState.DEFAULT && keys.size() <= CUTOFF)) {
				final Text build = new LiteralText("");
				build.append(CommandMessageFormatting.LT_TEXT);
				for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
					build.append(new LiteralText(it.next()));
					if (it.hasNext()) {
						build.append(CommandMessageFormatting.PIPE_TEXT);
					}
				}
				build.append(CommandMessageFormatting.GT_TEXT);
				return new LiteralText(build.getString());
			}
			return super.getUsage(commander);
		}
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

	private static class FirstParsingCommandElement extends CommandElement {
		private final List<CommandElement> elements;

		FirstParsingCommandElement(List<CommandElement> elements) {
			super(null);
			this.elements = elements;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			ArgumentParseException lastException = null;
			for (CommandElement element : this.elements) {
				CommandArgs.Snapshot startState = args.getSnapshot();
				CommandContext.Snapshot contextSnapshot = context.createSnapshot();
				try {
					element.parse(source, args, context);
					return;
				} catch (ArgumentParseException ex) {
					lastException = ex;
					args.applySnapshot(startState);
					context.applySnapshot(contextSnapshot);
				}
			}
			if (lastException != null) {
				throw lastException;
			}
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return null;
		}

		@Override
		public List<String> complete(final PermissibleCommandSource src, final CommandArgs args, final CommandContext context) {
			return Lists.newLinkedList(Iterables.concat(this.elements.stream().map(element -> {
				if (element == null) {
					return ImmutableList.<String>of();
				} else {
					CommandArgs.Snapshot snapshot = args.getSnapshot();
					List<String> ret = element.complete(src, args, context);
					args.applySnapshot(snapshot);
					return ret;
				}
			}).collect(Collectors.toSet())));
		}

		@Override
		public Text getUsage(PermissibleCommandSource commander) {
			final Text ret = new LiteralText("");
			for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext(); ) {
				ret.append(it.next().getUsage(commander));
				if (it.hasNext()) {
					ret.append(CommandMessageFormatting.PIPE_TEXT);
				}
			}
			return new LiteralText(ret.getString());
		}
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

	private static class OptionalCommandElement extends CommandElement {
		private final CommandElement element;
		@Nullable
		private final Object value;
		private final boolean considerInvalidFormatEmpty;

		OptionalCommandElement(CommandElement element, @Nullable Object value, boolean considerInvalidFormatEmpty) {
			super(null);
			this.element = element;
			this.value = value;
			this.considerInvalidFormatEmpty = considerInvalidFormatEmpty;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			if (!args.hasNext()) {
				Text key = this.element.getKey();
				if (key != null && this.value != null) {
					context.putArg(key.asString(), this.value);
				}
				return;
			}
			CommandArgs.Snapshot startState = args.getSnapshot();
			try {
				this.element.parse(source, args, context);
			} catch (ArgumentParseException ex) {
				if (this.considerInvalidFormatEmpty || args.hasNext()) { // If there are more args, suppress. Otherwise, throw the error
					args.applySnapshot(startState);
					if (this.element.getKey() != null && this.value != null) {
						context.putArg(this.element.getUntranslatedKey(), this.value);
					}
				} else {
					throw ex;
				}
			}
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return args.hasNext() ? null : this.element.parseValue(source, args);
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			return this.element.complete(src, args, context);
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			final Text containingUsage = this.element.getUsage(src);
			if (containingUsage.getString().isEmpty()) {
				return new LiteralText("");
			}
			return new LiteralText("[" + this.element.getUsage(src) + "]");
		}
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

	private static class RepeatedCommandElement extends CommandElement {
		private final CommandElement element;
		private final int times;


		protected RepeatedCommandElement(CommandElement element, int times) {
			super(null);
			this.element = element;
			this.times = times;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			for (int i = 0; i < this.times; ++i) {
				this.element.parse(source, args, context);
			}
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return null;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			for (int i = 0; i < this.times; ++i) {
				CommandArgs.Snapshot startState = args.getSnapshot();
				try {
					this.element.parse(src, args, context);
				} catch (ArgumentParseException e) {
					args.applySnapshot(startState);
					return this.element.complete(src, args, context);
				}
			}
			return Collections.emptyList();
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return new LiteralText(this.times + '*' + this.element.getUsage(src).getString());
		}
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

	private static class AllOfCommandElement extends CommandElement {
		private final CommandElement element;


		protected AllOfCommandElement(CommandElement element) {
			super(null);
			this.element = element;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			while (args.hasNext()) {
				this.element.parse(source, args, context);
			}
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return null;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			CommandArgs.Snapshot startState = null;
			try {
				while (args.hasNext()) {
					startState = args.getSnapshot();
					this.element.parse(src, args, context);
				}
			} catch (ArgumentParseException e) {
				// ignored
			}

			// The final element, if an exception was not thrown, might have more completions available to it.
			// Therefore, we reapply the last snapshot and complete from it.
			if (startState != null) {
				args.applySnapshot(startState);
			}

			if (args.canComplete()) {
				return this.element.complete(src, args, context);
			}

			// If we have more elements, do not complete.
			return Collections.emptyList();
		}

		@Override
		public Text getUsage(PermissibleCommandSource context) {
			return new LiteralText(this.element.getUsage(context).getString() + CommandMessageFormatting.STAR_TEXT.asString());
		}
	}

	// -- Argument types for basic java types

	/**
	 * Parent class that specifies elements as having no tab completions.
	 * Useful for inputs with a very large domain, like strings and integers.
	 */
	private abstract static class KeyElement extends CommandElement {
		private KeyElement(Text key) {
			super(key);
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			return Collections.emptyList();
		}
	}

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

	private static class StringElement extends KeyElement {

		StringElement(Text key) {
			super(key);
		}

		@Override
		public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return args.next();
		}
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

	private static class NumericElement<T extends Number> extends KeyElement {
		private final Function<String, T> parseFunc;
		@Nullable
		private final BiFunction<String, Integer, T> parseRadixFunction;
		private final Function<String, Text> errorSupplier;

		protected NumericElement(Text key, Function<String, T> parseFunc, @Nullable BiFunction<String, Integer, T> parseRadixFunction,
								 Function<String, Text> errorSupplier) {
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

	private static class EnumValueElement<T extends Enum<T>> extends PatternMatchingCommandElement {
		private final Class<T> type;
		private final Map<String, T> values;

		EnumValueElement(Text key, Class<T> type) {
			super(key);
			this.type = type;
			this.values = Arrays.stream(type.getEnumConstants())
					.collect(Collectors.toMap(value -> value.name().toLowerCase(),
							Function.identity(), (value, value2) -> {
								throw new UnsupportedOperationException(type.getCanonicalName() + " contains more than one enum constant "
										+ "with the same name, only differing by capitalization, which is unsupported.");
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

	private static class RemainingJoinedStringsCommandElement extends KeyElement {
		private final boolean raw;

		RemainingJoinedStringsCommandElement(Text key, boolean raw) {
			super(key);
			this.raw = raw;
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			if (this.raw) {
				args.next();
				String ret = args.getRaw().substring(args.getRawPosition());
				while (args.hasNext()) { // Consume remaining args
					args.next();
				}
				return ret;
			}
			final StringBuilder ret = new StringBuilder(args.next());
			while (args.hasNext()) {
				ret.append(' ').append(args.next());
			}
			return ret.toString();
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return new LiteralText("").append(CommandMessageFormatting.LT_TEXT.getString()).append(this.getKey()).append(CommandMessageFormatting.ELLIPSIS_TEXT).append(CommandMessageFormatting.GT_TEXT);
		}
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

	private static class LiteralCommandElement extends CommandElement {
		private final List<String> expectedArgs;
		@Nullable
		private final Object putValue;

		protected LiteralCommandElement(@Nullable Text key, List<String> expectedArgs, @Nullable Object putValue) {
			super(key);
			this.expectedArgs = ImmutableList.copyOf(expectedArgs);
			this.putValue = putValue;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
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

	private static class PlayerCommandElement extends SelectorCommandElement {

		private final boolean returnSource;

		protected PlayerCommandElement(Text key, boolean returnSource) {
			super(key);
			this.returnSource = returnSource;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			if (!args.hasNext() && this.returnSource) {
				return this.tryReturnSource(source, args);
			}

			CommandArgs.Snapshot state = args.getSnapshot();
			try {
				return StreamSupport.stream(((Iterable<Entity>) super.parseValue(source, args)).spliterator(), false).filter(e -> e instanceof PlayerEntity).collect(Collectors.toList());
			} catch (ArgumentParseException ex) {
				if (this.returnSource) {
					args.applySnapshot(state);
					return this.tryReturnSource(source, args);
				}
				throw ex;
			}
		}

		@Override
		protected Iterable<String> getChoices(PermissibleCommandSource source) {
			return MinecraftServer.getServer().getPlayerManager().getPlayers().stream().map(player -> player.getGameProfile().getName()).collect(Collectors.toSet());
		}

		@Override
		protected Object getValue(String choice) throws IllegalArgumentException {
			Optional<PlayerEntity> ret = MinecraftServer.getServer().getPlayerManager().getPlayers().stream().findFirst().map(Function.identity());
			if (!ret.isPresent()) {
				throw new IllegalArgumentException("Input value " + choice + " was not a player");
			}
			return ret.get();
		}

		private PlayerEntity tryReturnSource(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			if (source instanceof PlayerEntity) {
				return ((PlayerEntity) source);
			} else {
				throw args.createError(new LiteralText("No players matched and source was not a player!"));
			}
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return src != null && this.returnSource ? new LiteralText("[" + super.getUsage(src) + "]") : super.getUsage(src);
		}
	}

	/**
	 * A {@link Vec3d} command element.
	 *
	 * <p>It has the following syntax:</p>
	 *
	 * <blockquote><pre> x,y,z
	 * x y z.</pre></blockquote>
	 *
	 * <p>Each element can be relative to a location? so
	 * <tt>parseRelativeDouble()</tt> -- relative is ~(num)</p>
	 */
	private static class Vec3dCommandElement extends CommandElement {
		private static final ImmutableSet<String> SPECIAL_TOKENS = ImmutableSet.of("#target", "#me");

		protected Vec3dCommandElement(@Nullable Text key) {
			super(key);
		}

		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String xStr;
			String yStr;
			String zStr;
			xStr = args.next();
			if (xStr.contains(",")) {
				String[] split = xStr.split(",");
				if (split.length != 3) {
					throw args.createError(new LiteralText(String.format("Comma-separated location must have 3 elements, not %s", split.length)));
				}
				xStr = split[0];
				yStr = split[1];
				zStr = split[2];
			} else if (xStr.equalsIgnoreCase("#me")) {
				return source.getPos();
			} else {
				yStr = args.next();
				zStr = args.next();
			}
			double x = this.parseRelativeDouble(args, xStr, source.getPos().x);
			double y = this.parseRelativeDouble(args, yStr, source.getPos().y);
			double z = this.parseRelativeDouble(args, zStr, source.getPos().z);

			return new Vec3d(x, y, z);
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			Optional<String> arg = args.nextIfPresent();
			// Traverse through the possible arguments. We can't really complete arbitrary integers
			if (arg.isPresent()) {
				if (arg.get().startsWith("#")) {
					Optional<String> finalArg = arg;
					return SPECIAL_TOKENS.stream().filter((input) -> input.startsWith(finalArg.get())).collect(ImmutableList.toImmutableList());
				} else if (arg.get().contains(",") || !args.hasNext()) {
					return ImmutableList.of(arg.get());
				} else {
					arg = args.nextIfPresent();
					if (args.hasNext()) {
						return ImmutableList.of(args.nextIfPresent().get());
					}
					return ImmutableList.of(arg.get());
				}
			}
			return ImmutableList.of();
		}

		private double parseRelativeDouble(CommandArgs args, String arg, @Nullable Double relativeTo) throws ArgumentParseException {
			boolean relative = arg.startsWith("~");
			if (relative) {
				if (relativeTo == null) {
					throw args.createError(new LiteralText("Relative position specified but source does not have a position"));
				}
				arg = arg.substring(1);
				if (arg.isEmpty()) {
					return relativeTo;
				}
			}
			try {
				double ret = Double.parseDouble(arg);
				return relative ? ret + relativeTo : ret;
			} catch (NumberFormatException e) {
				throw args.createError(new LiteralText(String.format("Expected input %s to be a double, but was not", arg)));
			}
		}
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

	private static class OnlyOneCommandElement extends CommandElement {
		private final CommandElement element;

		protected OnlyOneCommandElement(CommandElement element) {
			super(element.getKey());
			this.element = element;
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			this.element.parse(source, args, context);
			if (context.getAll(this.element.getUntranslatedKey()).size() > 1) {
				Text key = this.element.getKey();
				throw args.createError(new LiteralText(String.format("Argument %s may have only one value!", key != null ? key : "unknown")));
			}
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return this.element.getUsage(src);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return this.element.parseValue(source, args);
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			return this.element.complete(src, args, context);
		}
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

	private static class PermissionCommandElement extends CommandElement {
		private final CommandElement element;
		private final String permission;
		private final boolean isOptional;

		protected PermissionCommandElement(CommandElement element, String permission, boolean isOptional) {
			super(element.getKey());
			this.element = element;
			this.permission = permission;
			this.isOptional = isOptional;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			if (this.checkPermission(source, args)) {
				return this.element.parseValue(source, args);
			}

			return null;
		}

		private boolean checkPermission(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			// TODO
			boolean hasPermission = /*source.hasPermission(this.permission)*/ true;
			if (!hasPermission && !this.isOptional) {
				Text key = this.getKey();
				throw args.createError(new LiteralText(String.format("You do not have permission to use the %s argument", key != null ? key : "unknown")));
			}
			return hasPermission;
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			boolean flag = /*src.hasPermission(this.permission)*/ true;
			if (!flag) {
				return ImmutableList.of();
			}
			return this.element.complete(src, args, context);
		}

		@Override
		public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
			if (this.checkPermission(source, args)) {
				this.element.parse(source, args, context);
			}
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			if (this.isOptional && !src.hasPermission(this.permission)) {
				return new LiteralText("");
			}
			return this.element.getUsage(src);
		}
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

	private static class EntityCommandElement extends SelectorCommandElement {
		private final boolean returnTarget;
		private final boolean returnSource;
		@Nullable
		private final Class<? extends Entity> clazz;

		protected EntityCommandElement(Text key, boolean returnSource, boolean returnTarget, @Nullable Class<? extends Entity> clazz) {
			super(key);
			this.returnSource = returnSource;
			this.returnTarget = returnTarget;
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			if (!args.hasNext()) {
				if (this.returnSource) {
					return this.tryReturnSource(source, args, true);
				}
				if (this.returnTarget) {
					return this.tryReturnTarget(source, args);
				}
			}

			CommandArgs.Snapshot state = args.getSnapshot();
			try {
				Iterable<Entity> entities = (Iterable<Entity>) super.parseValue(source, args);
				for (Entity entity : entities) {
					if (!this.checkEntity(entity)) {
						Text name = new LiteralText(this.clazz == null ? "null" : this.clazz.getSimpleName());
						throw args.createError(new LiteralText("The entity is not of the required type! (").append(name).append(")"));
					}
				}
				return entities;
			} catch (ArgumentParseException ex) {
				if (this.returnSource) {
					args.applySnapshot(state);
					return this.tryReturnSource(source, args, true);
				}
				throw ex;
			}
		}

		@Override
		protected Iterable<String> getChoices(PermissibleCommandSource source) {
			Set<String> worldEntities = Arrays.stream(MinecraftServer.getServer().worlds).flatMap(world -> world.entities.stream())
					.filter(this::checkEntity)
					.map(entity -> entity.getUuid().toString()).collect(Collectors.toSet());
			Collection<PlayerEntity> players = Sets.newHashSet(MinecraftServer.getServer().getPlayerManager().getPlayers());
			if (!players.isEmpty() && this.checkEntity(players.iterator().next())) {
				final Set<String> setToReturn = Sets.newHashSet(worldEntities); // to ensure mutability
				players.forEach(x -> setToReturn.add(x.getTranslationKey()));
				return setToReturn;
			}

			return worldEntities;
		}

		@Override
		protected Object getValue(String choice) throws IllegalArgumentException {
			UUID uuid;
			try {
				uuid = UUID.fromString(choice);
			} catch (IllegalArgumentException ignored) {
				// Player could be a name
				return Optional.ofNullable(MinecraftServer.getServer().getPlayerManager().getPlayer(choice)).orElseThrow(() -> new IllegalArgumentException("Input value " + choice + " does not represent a valid entity"));
			}
			boolean found = false;
			Optional<Entity> ret = Optional.ofNullable(MinecraftServer.getServer().getEntity(uuid));
			if (ret.isPresent()) {
				Entity entity = ret.get();
				if (this.checkEntity(entity)) {
					return entity;
				}
				found = true;
			}
			if (found) {
				throw new IllegalArgumentException("Input value " + choice + " was not an entity of the required type!");
			}
			throw new IllegalArgumentException("Input value " + choice + " was not an entity");
		}

		private Entity tryReturnSource(PermissibleCommandSource source, CommandArgs args, boolean check) throws ArgumentParseException {
			if (source instanceof Entity && (!check || this.checkEntity((Entity) source))) {
				return (Entity) source;
			}
			throw args.createError(new LiteralText("No entities matched and source was not an entity!"));
		}

		// TODO
		private Entity tryReturnTarget(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			Entity entity = this.tryReturnSource(source, args, false);
			throw args.createError(new LiteralText("No entities matched and source was not looking at a valid entity!"));
//            return entity.getWorld().getIntersectingEntities(entity, 10).stream()
//                    .filter(e -> !e.getEntity().equals(entity)).map(EntityUniverse.EntityHit::getEntity)
//                    .filter(this::checkEntity).findFirst()
//                    .orElseThrow(() -> args.createError(new LiteralText("No entities matched and source was not looking at a valid entity!")));
		}

		private boolean checkEntity(Entity entity) {
			if (this.clazz == null) {
				return true;
			} else {
				return this.clazz.isAssignableFrom(entity.getClass());
			}
		}

		@Override
		public Text getUsage(PermissibleCommandSource src) {
			return src instanceof Entity && (this.returnSource || this.returnTarget) ? new LiteralText("[" + this.getKey().getString() + "]") : super.getUsage(src);
		}
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

	private static class UrlElement extends KeyElement {

		protected UrlElement(Text key) {
			super(key);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String str = args.next();
			URL url;
			try {
				url = new URL(str);
			} catch (MalformedURLException ex) {
				throw new ArgumentParseException(new LiteralText("Invalid URL!"), ex, str, 0);
			}
			try {
				url.toURI();
			} catch (URISyntaxException ex) {
				throw new ArgumentParseException(new LiteralText("Invalid URL!"), ex, str, 0);
			}
			return url;
		}
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

	private static class IpElement extends KeyElement {
		private final PlayerCommandElement possiblePlayer;

		protected IpElement(Text key) {
			super(key);
			this.possiblePlayer = new PlayerCommandElement(key, false);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String s = args.next();
			try {
				return InetAddress.getByName(s);
			} catch (UnknownHostException e) {
				try {
					return ((ServerPlayerEntity) this.possiblePlayer.parseValue(source, args)).networkHandler.getConnection().getAddress();
				} catch (ArgumentParseException ex) {
					throw args.createError(new LiteralText("Invalid IP address!"));
				}
			}
		}
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

	private static class BigDecimalElement extends KeyElement {

		protected BigDecimalElement(Text key) {
			super(key);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String next = args.next();
			try {
				return new BigDecimal(next);
			} catch (NumberFormatException ex) {
				throw args.createError(new LiteralText("Expected a number, but input " + next + " was not"));
			}
		}
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

	private static class BigIntegerElement extends KeyElement {

		protected BigIntegerElement(Text key) {
			super(key);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String integerString = args.next();
			try {
				return new BigInteger(integerString);
			} catch (NumberFormatException ex) {
				throw args.createError(new LiteralText("Expected an integer, but input " + integerString + " was not"));
			}
		}
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

	private static class UuidElement extends KeyElement {

		protected UuidElement(Text key) {
			super(key);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			try {
				return UUID.fromString(args.next());
			} catch (IllegalArgumentException ex) {
				throw args.createError(new LiteralText("Invalid UUID!"));
			}
		}

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

	private static class StringCommandElement extends KeyElement {
		private final StringType stringType;
		private final RemainingJoinedStringsCommandElement joinedElement;

		private StringCommandElement(Text key, StringType type) {
			super(key);
			this.stringType = type;
			this.joinedElement = type.isAll() ? new RemainingJoinedStringsCommandElement(key, false) : null;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return this.stringType.isAll() ? (String) this.joinedElement.parseValue(source, args) : args.next();
		}
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

	private static class DateTimeElement extends CommandElement {

		private final boolean returnNow;

		protected DateTimeElement(Text key, boolean returnNow) {
			super(key);
			this.returnNow = returnNow;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
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
						throw args.createError(new LiteralText("Invalid date-time!"));
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
				return new LiteralText("[" + this.getKey().getString() + "]");
			}
		}
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

	private static class DurationElement extends KeyElement {

		protected DurationElement(Text key) {
			super(key);
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			String s = args.next().toUpperCase();
			if (!s.contains("T")) {
				if (s.contains("D")) {
					if (s.contains("H") || s.contains("M") || s.contains("S")) {
						s = s.replace("D", "DT");
					}
				} else {
					if (s.startsWith("P")) {
						s = "PT" + s.substring(1);
					} else {
						s = "T" + s;
					}
				}
			}
			if (!s.startsWith("P")) {
				s = "P" + s;
			}
			try {
				return Duration.parse(s);
			} catch (DateTimeParseException ex) {
				throw args.createError(new LiteralText("Invalid duration!"));
			}
		}
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

	private static class WithSuggestionsElement extends CommandElement {

		private final CommandElement wrapped;
		private final Function<PermissibleCommandSource, Iterable<String>> suggestions;
		private final boolean requireBegin;

		protected WithSuggestionsElement(CommandElement wrapped, Function<PermissibleCommandSource, Iterable<String>> suggestions, boolean requireBegin) {
			super(wrapped.getKey());
			this.wrapped = wrapped;
			this.suggestions = suggestions;
			this.requireBegin = requireBegin;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
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

	private static class FilteredSuggestionsElement extends CommandElement {
		private final CommandElement wrapped;
		private final Predicate<String> predicate;

		protected FilteredSuggestionsElement(CommandElement wrapped, Predicate<String> predicate) {
			super(wrapped.getKey());
			this.wrapped = wrapped;
			this.predicate = predicate;
		}

		@Nullable
		@Override
		protected Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
			return this.wrapped.parseValue(source, args);
		}

		@Override
		public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
			return this.wrapped.complete(src, args, context).stream().filter(this.predicate).collect(ImmutableList.toImmutableList());
		}
	}
}
