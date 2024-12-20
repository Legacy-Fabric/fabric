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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandNotFoundException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.ImmutableCommandMapping;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.InvocationCommandException;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

/**
 * A simple implementation of a {@link Dispatcher}.
 */
public final class SimpleDispatcher implements Dispatcher {
	/**
	 * This is a disambiguator function that returns the first matching command.
	 */
	public static final Disambiguator FIRST_DISAMBIGUATOR = (source, aliasUsed, availableOptions) -> {
		for (CommandMapping mapping : availableOptions) {
			if (mapping.getPrimaryAlias().toLowerCase().equals(aliasUsed.toLowerCase())) {
				return Optional.of(mapping);
			}
		}

		return Optional.of(availableOptions.get(0));
	};

	private final Disambiguator disambiguatorFunc;
	private final ListMultimap<String, CommandMapping> commands = ArrayListMultimap.create();

	/**
	 * Creates a basic new dispatcher.
	 */
	public SimpleDispatcher() {
		this(FIRST_DISAMBIGUATOR);
	}

	/**
	 * Creates a new dispatcher with a specific disambiguator.
	 *
	 * @param disambiguatorFunc Function that returns the preferred command if
	 *                          multiple exist for a given alias
	 */
	public SimpleDispatcher(Disambiguator disambiguatorFunc) {
		this.disambiguatorFunc = disambiguatorFunc;
	}

	/**
	 * Register a given command using the given list of aliases.
	 *
	 * <p>If there is a conflict with one of the aliases (i.e. that alias
	 * is already assigned to another command), then the alias will be skipped.
	 * It is possible for there to be no alias to be available out of
	 * the provided list of aliases, which would mean that the command would not
	 * be assigned to any aliases.</p>
	 *
	 * <p>The first non-conflicted alias becomes the "primary alias."</p>
	 *
	 * @param callable The command
	 * @param alias    An array of aliases
	 * @return The registered command mapping, unless no aliases could be
	 * registered
	 */
	public Optional<CommandMapping> register(CommandCallable callable, String... alias) {
		Preconditions.checkNotNull(alias, "alias");
		return this.register(callable, Arrays.asList(alias));
	}

	/**
	 * Register a given command using the given list of aliases.
	 *
	 * <p>If there is a conflict with one of the aliases (i.e. that alias
	 * is already assigned to another command), then the alias will be skipped.
	 * It is possible for there to be no alias to be available out of
	 * the provided list of aliases, which would mean that the command would not
	 * be assigned to any aliases.</p>
	 *
	 * <p>The first non-conflicted alias becomes the "primary alias."</p>
	 *
	 * @param callable The command
	 * @param aliases  A list of aliases
	 * @return The registered command mapping, unless no aliases could be
	 * registered
	 */
	public Optional<CommandMapping> register(CommandCallable callable, List<String> aliases) {
		return this.register(callable, aliases, Function.identity());
	}

	/**
	 * Register a given command using a given list of aliases.
	 *
	 * <p>The provided callback function will be called with a list of aliases
	 * that are not taken (from the list of aliases that were requested) and
	 * it should return a list of aliases to actually register. Aliases may be
	 * removed, and if no aliases remain, then the command will not be
	 * registered. It may be possible that no aliases are available, and thus
	 * the callback would receive an empty list. New aliases should not be added
	 * to the list in the callback as this may cause
	 * {@link IllegalArgumentException} to be thrown.</p>
	 *
	 * <p>The first non-conflicted alias becomes the "primary alias."</p>
	 *
	 * @param callable The command
	 * @param aliases  A list of aliases
	 * @param callback The callback
	 * @return The registered command mapping, unless no aliases could
	 * be registered
	 */
	public synchronized Optional<CommandMapping> register(CommandCallable callable, List<String> aliases, Function<List<String>, List<String>> callback) {
		Preconditions.checkNotNull(aliases, "aliases");
		Preconditions.checkNotNull(callable, "callable");
		Preconditions.checkNotNull(callback, "callback");

		// Invoke the callback with the commands that /can/ be registered
		aliases = ImmutableList.copyOf(callback.apply(aliases));

		if (aliases.isEmpty()) {
			return Optional.empty();
		}

		String primary = aliases.get(0);
		List<String> secondary = aliases.subList(1, aliases.size());
		CommandMapping mapping = new ImmutableCommandMapping(callable, primary, secondary);

		for (String alias : aliases) {
			this.commands.put(alias.toLowerCase(), mapping);
		}

		return Optional.of(mapping);
	}

	/**
	 * Remove a mapping identified by the given alias.
	 *
	 * @param alias The alias
	 * @return The previous mapping associated with the alias, if one was found
	 */
	public synchronized Collection<CommandMapping> remove(String alias) {
		return this.commands.removeAll(alias.toLowerCase());
	}

	/**
	 * Remove all mappings identified by the given aliases.
	 *
	 * @param aliases A collection of aliases
	 * @return Whether any were found
	 */
	public synchronized boolean removeAll(Collection<?> aliases) {
		Preconditions.checkNotNull(aliases, "aliases");

		boolean found = false;

		for (Object alias : aliases) {
			if (!this.commands.removeAll(alias.toString().toLowerCase()).isEmpty()) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * Remove a command identified by the given mapping.
	 *
	 * @param mapping The mapping
	 * @return The previous mapping associated with the alias, if one was found
	 */
	public synchronized Optional<CommandMapping> removeMapping(CommandMapping mapping) {
		Preconditions.checkNotNull(mapping, "mapping");

		CommandMapping found = null;

		Iterator<CommandMapping> it = this.commands.values().iterator();

		while (it.hasNext()) {
			CommandMapping current = it.next();

			if (current.equals(mapping)) {
				it.remove();
				found = current;
			}
		}

		return Optional.ofNullable(found);
	}

	/**
	 * Remove all mappings contained with the given collection.
	 *
	 * @param mappings The collection
	 * @return Whether the at least one command was removed
	 */
	public synchronized boolean removeMappings(Collection<?> mappings) {
		Preconditions.checkNotNull(mappings, "mappings");
		boolean found = false;
		Iterator<CommandMapping> it = this.commands.values().iterator();

		while (it.hasNext()) {
			if (mappings.contains(it.next())) {
				it.remove();
				found = true;
			}
		}

		return found;
	}

	@Override
	public synchronized Set<CommandMapping> getCommands() {
		return ImmutableSet.copyOf(this.commands.values());
	}

	@Override
	public synchronized Set<String> getPrimaryAliases() {
		Set<String> aliases = new HashSet<>();

		for (CommandMapping mapping : this.commands.values()) {
			aliases.add(mapping.getPrimaryAlias());
		}

		return Collections.unmodifiableSet(aliases);
	}

	@Override
	public synchronized Set<String> getAliases() {
		Set<String> aliases = new HashSet<>();

		for (CommandMapping mapping : this.commands.values()) {
			aliases.addAll(mapping.getAllAliases());
		}

		return Collections.unmodifiableSet(aliases);
	}

	@Override
	public Optional<CommandMapping> get(String alias) {
		return this.get(alias, null);
	}

	@Override
	public synchronized Optional<CommandMapping> get(String alias, @Nullable PermissibleCommandSource source) {
		List<CommandMapping> results = this.commands.get(alias.toLowerCase());
		Optional<CommandMapping> result = Optional.empty();

		if (results.size() == 1) {
			result = Optional.of(results.get(0));
		} else if (results.size() > 1) {
			result = this.disambiguatorFunc.disambiguate(source, alias, results);
		}

		if (source != null) {
			result = result.filter(m -> m.getCallable().testPermission(source));
		}

		return result;
	}

	@Override
	public synchronized boolean containsAlias(String alias) {
		return this.commands.containsKey(alias.toLowerCase());
	}

	@Override
	public boolean containsMapping(CommandMapping mapping) {
		Preconditions.checkNotNull(mapping, "mapping");

		for (CommandMapping test : this.commands.values()) {
			if (mapping.equals(test)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public CommandResult process(PermissibleCommandSource source, String commandLine) throws CommandException {
		final String[] argSplit = commandLine.split(" ", 2);
		Optional<CommandMapping> cmdOptional = this.get(argSplit[0], source);

		if (!cmdOptional.isPresent()) {
			throw new CommandNotFoundException(ChatMessage.createTranslateMessage("commands.generic.notFound"), argSplit[0]);
		}

		final String arguments = argSplit.length > 1 ? argSplit[1] : "";
		CommandMapping mapping = cmdOptional.get();
		final CommandCallable spec = mapping.getCallable();

		try {
			return spec.process(source, arguments);
		} catch (CommandNotFoundException e) {
			throw new CommandException(ChatMessage.createTextMessage("No such child command: " + e.getCommand()));
		} catch (RuntimeException e) {
			throw new InvocationCommandException(ChatMessage.createTextMessage("An unexpected error happened executing the command"), e);
		}
	}

	@Override
	public List<String> getSuggestions(PermissibleCommandSource src, final String arguments, @Nullable Location<World> targetPosition) throws CommandException {
		final String[] argSplit = arguments.split(" ", 2);
		Optional<CommandMapping> cmdOptional = this.get(argSplit[0], src);

		if (argSplit.length == 1) {
			return Collections.unmodifiableList(new ArrayList<>(this.filterCommands(src, argSplit[0])));
		} else if (!cmdOptional.isPresent()) {
			return ImmutableList.of();
		}

		return cmdOptional.get().getCallable().getSuggestions(src, argSplit[1], targetPosition);
	}

	@Override
	public boolean testPermission(PermissibleCommandSource source) {
		for (CommandMapping mapping : this.commands.values()) {
			if (mapping.getCallable().testPermission(source)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Optional<ChatMessage> getShortDescription(PermissibleCommandSource source) {
		return Optional.empty();
	}

	@Override
	public Optional<ChatMessage> getHelp(PermissibleCommandSource source) {
		if (this.commands.isEmpty()) {
			return Optional.empty();
		}

		ChatMessage build = ChatMessage.createTextMessage("Available commands:\n");

		for (Iterator<String> it = this.filterCommands(source).iterator(); it.hasNext(); ) {
			final Optional<CommandMapping> mappingOpt = this.get(it.next(), source);

			if (!mappingOpt.isPresent()) {
				continue;
			}

			CommandMapping mapping = mappingOpt.get();
			final Optional<ChatMessage> description = mapping.getCallable().getShortDescription(source);
			ChatMessage text = ChatMessage.createTextMessage(mapping.getPrimaryAlias());
			build.addUsing(text.setColor(Formatting.GREEN).setUnderlined(Boolean.TRUE)
			/*	Click Event not supported in this version
			.setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/" + mapping.getPrimaryAlias())))
			*/).addUsing(CommandMessageFormatting.SPACE_TEXT).addUsing(description.orElse(mapping.getCallable().getUsage(source)));

			if (it.hasNext()) {
				build.addText("\n");
			}
		}

		return Optional.of(ChatMessage.createTextMessage(build.toString()));
	}

	private Set<String> filterCommands(final PermissibleCommandSource src) {
		return Multimaps.filterValues(this.commands, input -> input.getCallable().testPermission(src)).keys().elementSet();
	}

	// Filter out commands by String first
	private Set<String> filterCommands(final PermissibleCommandSource src, String start) {
		Multimap<String, CommandMapping> map = Multimaps.filterKeys(this.commands,
				input -> input != null && input.toLowerCase().startsWith(start.toLowerCase()));
		return Multimaps.filterValues(map, input -> input.getCallable().testPermission(src)).keys().elementSet();
	}

	/**
	 * Gets the number of registered aliases.
	 *
	 * @return The number of aliases
	 */
	public synchronized int size() {
		return this.commands.size();
	}

	@Override
	public ChatMessage getUsage(final PermissibleCommandSource source) {
		final StringBuilder build = new StringBuilder();
		Iterable<String> filteredCommands = this.filterCommands(source).stream()
				.filter(input -> {
					if (input == null) {
						return false;
					}

					final Optional<CommandMapping> ret = this.get(input, source);
					return ret.isPresent() && ret.get().getPrimaryAlias().equals(input);
				})
				.collect(Collectors.toList());

		for (Iterator<String> it = filteredCommands.iterator(); it.hasNext(); ) {
			build.append(it.next());

			if (it.hasNext()) {
				build.append(CommandMessageFormatting.PIPE_TEXT);
			}
		}

		return ChatMessage.createTextMessage(build.toString());
	}

	@Override
	public synchronized Set<CommandMapping> getAll(String alias) {
		return ImmutableSet.copyOf(this.commands.get(alias));
	}

	@Override
	public Multimap<String, CommandMapping> getAll() {
		return ImmutableMultimap.copyOf(this.commands);
	}
}
