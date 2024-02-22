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

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

/**
 * Context that a command is executed in.
 * This object stores parsed arguments from other commands
 */
public final class CommandContext {
	/**
	 * The argument key for a target block position that may be present
	 * during tab completion, of type {@link Location Location&lt;World&gt;}.
	 */
	public static final String TARGET_BLOCK_ARG = "targetblock-pos048658"; // Random junk afterwards so we don't accidentally conflict with other args

	/**
	 * The argument key to indicate that a tab completion is taking place.
	 */
	public static final String TAB_COMPLETION = "tab-complete-50456"; // Random junk afterwards so we don't accidentally conflict with other args

	private final Multimap<String, Object> parsedArgs;
	private final Set<String> definedFlags;

	/**
	 * Create a new empty CommandContext.
	 */
	public CommandContext() {
		this.parsedArgs = ArrayListMultimap.create();
		this.definedFlags = Sets.newHashSet();
	}

	/**
	 * Gets all values for the given argument. May return an empty list if no
	 * values are present.
	 *
	 * @param key The key to get values for
	 * @param <T> the type of value to get
	 * @return the collection of all values
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getAll(String key) {
		return Collections.unmodifiableCollection((Collection<T>) this.parsedArgs.get(key));
	}

	/**
	 * Gets all values for the given argument. May return an empty list if no
	 * values are present.
	 *
	 * @param key The key to get values for
	 * @param <T> the type of value to get
	 * @return the collection of all values
	 */
	public <T> Collection<T> getAll(Text key) {
		return this.getAll(CommandContext.textToArgKey(key));
	}

	/**
	 * Gets the value for the given key if the key has only one value.
	 *
	 * <p>An empty {@link Optional} indicates that there are either zero or more
	 * than one values for the given key. Use {@link #hasAny(Text)} to verify
	 * which.</p>
	 *
	 * @param key the key to get
	 * @param <T> the expected type of the argument
	 * @return the argument
	 */
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getOne(String key) {
		Collection<Object> values = this.parsedArgs.get(key);

		if (values.size() != 1) {
			return Optional.empty();
		}

		return Optional.ofNullable((T) values.iterator().next());
	}

	/**
	 * Gets the value for the given key if the key has only one value.
	 *
	 * <p>An empty {@link Optional} indicates that there are either zero or more
	 * than one values for the given key. Use {@link #hasAny(Text)} to verify
	 * which.</p>
	 *
	 * @param key the key to get
	 * @param <T> the expected type of the argument
	 * @return the argument
	 */
	public <T> Optional<T> getOne(Text key) {
		return this.getOne(CommandContext.textToArgKey(key));
	}

	/**
	 * Gets the value for the given key if the key has only one value, throws an
	 * exception otherwise.
	 *
	 * @param key the key to get
	 * @param <T> the expected type of the argument
	 * @return the argument
	 * @throws NoSuchElementException   if there is no element with the
	 *                                  associated key
	 * @throws IllegalArgumentException if there are more than one element
	 *                                  associated with the key (thus, the argument is illegal in this
	 *                                  context)
	 * @throws ClassCastException       if the element type is not what is expected
	 *                                  by the caller
	 */
	@SuppressWarnings("unchecked")
	public <T> T requireOne(String key) throws NoSuchElementException, IllegalArgumentException, ClassCastException {
		Collection<Object> values = this.parsedArgs.get(key);

		if (values.size() == 1) {
			return (T) values.iterator().next();
		} else if (values.isEmpty()) {
			throw new NoSuchElementException();
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Gets the value for the given key if the key has only one value, throws an
	 * exception otherwise.
	 *
	 * @param key the key to get
	 * @param <T> the expected type of the argument
	 * @return the argument
	 * @throws NoSuchElementException   if there is no element with the
	 *                                  associated key
	 * @throws IllegalArgumentException if there are more than one element
	 *                                  associated with the key (thus, the argument is illegal in this
	 *                                  context)
	 * @throws ClassCastException       if the element type is not what is expected
	 */
	public <T> T requireOne(Text key)
			throws NoSuchElementException, IllegalArgumentException, ClassCastException {
		return this.requireOne(CommandContext.textToArgKey(key));
	}

	/**
	 * Insert an argument into this context.
	 *
	 * @param key   the key to store the arg under
	 * @param value the value for this argument
	 */
	public void putArg(String key, Object value) {
		Preconditions.checkNotNull(value, "value");
		this.parsedArgs.put(key, value);
	}

	/**
	 * Insert an argument into this context.
	 *
	 * @param key   the key to store the arg under
	 * @param value the value for this argument
	 */
	public void putArg(Text key, Object value) {
		this.putArg(CommandContext.textToArgKey(key), value);
	}

	/**
	 * Defines the flag as being present when parsing this context.
	 *
	 * @param key the key for the flag defined
	 */
	public void addFlag(String key) {
		this.definedFlags.add(key);
	}

	/**
	 * Defines the flag as being present when parsing this context.
	 *
	 * @param key the key for the flag defined
	 */
	public void addFlag(Text key) {
		this.addFlag(CommandContext.textToArgKey(key));
	}

	/**
	 * Perform a permissions check, throwing an exception if the required
	 * permissions are not present.
	 *
	 * @param commander  the source to check against
	 * @param permission The permission to check
	 * @throws CommandException if the source does not have permission
	 */
	public void checkPermission(PermissibleCommandSource commander, String permission) throws CommandException {
		if (!commander.hasPermission(permission)) {
			throw new CommandException(new LiteralText("You do not have permission to use this command!"));
		}
	}

	/**
	 * Returns whether this context has any value for the given argument key.
	 *
	 * @param key The key to look up
	 * @return whether there are any values present
	 */
	public boolean hasAny(String key) {
		return this.parsedArgs.containsKey(key);
	}

	/**
	 * Returns whether this context has any value for the given argument key.
	 *
	 * @param key The key to look up
	 * @return whether there are any values present
	 */
	public boolean hasAny(Text key) {
		return this.hasAny(CommandContext.textToArgKey(key));
	}

	/**
	 * Returns whether the given flag has been defined in this context.
	 *
	 * @param key The key to look up
	 * @return whether the flag is defined
	 */
	public boolean hasFlag(String key) {
		return this.definedFlags.contains(key);
	}

	/**
	 * Returns whether the given flag has been defined in this context.
	 *
	 * @param key The key to look up
	 * @return whether the flag is defined
	 */
	public boolean hasFlag(Text key) {
		return this.hasFlag(CommandContext.textToArgKey(key));
	}

	/**
	 * Gets a snapshot of the data inside this context to allow it to be
	 * restored later.
	 *
	 * <p>This is only guaranteed to create a <em>shallow copy</em> of the
	 * backing store. If any value is mutable, any changes to that value
	 * will be reflected in this snapshot. It is therefore not recommended
	 * that you keep this snapshot around for longer than is necessary.</p>
	 *
	 * @return The {@link Snapshot} containing the current state of the
	 * {@link CommandContext}
	 */
	public Snapshot createSnapshot() {
		return new Snapshot(this.parsedArgs, this.definedFlags);
	}

	/**
	 * Resets a {@link CommandContext} to a previous state using a previously
	 * created {@link Snapshot}.
	 *
	 * @param snapshot The {@link Snapshot} to restore this context with
	 */
	public void applySnapshot(Snapshot snapshot) {
		this.parsedArgs.clear();
		this.parsedArgs.putAll(snapshot.args);
		this.definedFlags.clear();
		this.definedFlags.addAll(snapshot.flags);
	}

	/**
	 * A snapshot of a {@link CommandContext}. This object does not contain any
	 * public API methods, a snapshot should be considered a black box.
	 */
	public static final class Snapshot {
		final Multimap<String, Object> args;
		final Set<String> flags;

		Snapshot(Multimap<String, Object> args, Set<String> flags) {
			this.args = ArrayListMultimap.create(args);
			this.flags = Sets.newHashSet(flags);
		}
	}

	/**
	 * Converts a {@link Text} into a String key.
	 *
	 * @param key the text to be converted into a string key
	 * @return the string key. if {@code key} is a {@link TranslatableText}, the translation key.
	 */
	public static String textToArgKey(@Nullable Text key) {
		if (key == null) {
			return null;
		}

		if (key instanceof TranslatableText) { // Use translation key
			return ((TranslatableText) key).getKey();
		}

		return key.computeValue();
	}
}
