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

package net.legacyfabric.fabric.api.command.v2.lib.sponge;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.World;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher.Dispatcher;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

/**
 * A command dispatcher watches for commands (such as those said in chat)
 * and dispatches them to the correct command handler.
 */
public interface CommandManager extends Dispatcher {
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
	Optional<CommandMapping> register(CommandCallable callable, String... alias);

	/**
	 * Register a given command using the given list of aliases.
	 *
	 * <p>If there is a conflict with one of the aliases (i.e. that alias
	 * is already assigned to another command), then the alias will be skipped.
	 * It is possible for there to be no alias to be available out of
	 * the provided list of aliases, which would mean that the command would
	 * not be assigned to any aliases.</p>
	 *
	 * <p>The first non-conflicted alias becomes the "primary alias."</p>
	 *
	 * @param callable The command
	 * @param aliases  A list of aliases
	 * @return The registered command mapping, unless no aliases could be
	 * registered
	 * @throws IllegalArgumentException Thrown if {@code plugin} is not a
	 *                                  plugin instance
	 */
	Optional<CommandMapping> register(CommandCallable callable, List<String> aliases);

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
	 * @return The registered command mapping, unless no aliases could be
	 * registered
	 * @throws IllegalArgumentException Thrown if new conflicting aliases are
	 *                                  added in the callback
	 */
	Optional<CommandMapping> register(CommandCallable callable, List<String> aliases, Function<List<String>, List<String>> callback);

	/**
	 * Remove a command identified by the given mapping.
	 *
	 * @param mapping The mapping
	 * @return The previous mapping associated with the alias, if one was found
	 */
	Optional<CommandMapping> removeMapping(CommandMapping mapping);

	/**
	 * Gets the number of registered aliases.
	 *
	 * @return The number of aliases
	 */
	int size();

	/**
	 * Execute the command based on input arguments.
	 *
	 * <p>The implementing class must perform the necessary permission
	 * checks.</p>
	 *
	 * @param source    The caller of the command
	 * @param arguments The raw arguments for this command
	 * @return The result of a command being processed
	 */
	@Override
	CommandResult process(PermissibleCommandSource source, String arguments);

	/**
	 * Gets a list of suggestions based on input.
	 *
	 * <p>If a suggestion is chosen by the user, it will replace the last
	 * word.</p>
	 *
	 * @param source    The command source
	 * @param arguments The arguments entered up to this point
	 * @return A list of suggestions
	 */
	@Override
	List<String> getSuggestions(PermissibleCommandSource source, String arguments, @Nullable Location<World> targetPosition);
}
