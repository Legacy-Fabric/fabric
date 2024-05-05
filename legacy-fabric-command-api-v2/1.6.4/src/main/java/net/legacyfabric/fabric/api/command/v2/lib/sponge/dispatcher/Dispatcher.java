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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Multimap;
import org.jetbrains.annotations.Nullable;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

/**
 * Executes a command based on user input.
 */
public interface Dispatcher extends CommandCallable {
	/**
	 * Gets a list of commands. Each command, regardless of how many aliases it
	 * may have, will only appear once in the returned set.
	 *
	 * <p>The returned collection cannot be modified.</p>
	 *
	 * @return A list of registrations
	 */
	Set<? extends CommandMapping> getCommands();

	/**
	 * Gets a list of primary aliases.
	 *
	 * <p>The returned collection cannot be modified.</p>
	 *
	 * @return A list of aliases
	 */
	Set<String> getPrimaryAliases();

	/**
	 * Gets a list of all the command aliases, which includes the primary alias.
	 *
	 * <p>A command may have more than one alias assigned to it. The returned
	 * collection cannot be modified.</p>
	 *
	 * @return A list of aliases
	 */
	Set<String> getAliases();

	/**
	 * Gets the {@link CommandMapping} associated with an alias. Returns null if
	 * no command is named by the given alias.
	 *
	 * @param alias The alias
	 * @return The command mapping, if available
	 */
	Optional<? extends CommandMapping> get(String alias);

	/**
	 * Gets the {@link CommandMapping} associated with an alias in the context
	 * of a given {@link PermissibleCommandSource}. Returns null if no command is named by
	 * the given alias.
	 *
	 * @param alias  The alias to look up
	 * @param source The source this alias is being looked up for
	 * @return The command mapping, if available
	 */
	Optional<? extends CommandMapping> get(String alias, @Nullable PermissibleCommandSource source);

	/**
	 * Gets all the {@link CommandMapping}s associated with an alias.
	 *
	 * @param alias The alias
	 * @return The command mappings associated with the alias
	 */
	Set<? extends CommandMapping> getAll(String alias);

	/**
	 * Gets all commands currently registered with this dispatcher. The returned
	 * value is immutable.
	 *
	 * @return a multimap from alias to mapping of every registered command
	 */
	Multimap<String, CommandMapping> getAll();

	/**
	 * Returns whether the dispatcher contains a registered command for the
	 * given alias.
	 *
	 * @param alias The alias
	 * @return True if a registered command exists
	 */
	boolean containsAlias(String alias);

	/**
	 * Returns whether the dispatcher contains the given mapping.
	 *
	 * @param mapping The mapping
	 * @return True if a mapping exists
	 */
	boolean containsMapping(CommandMapping mapping);
}
