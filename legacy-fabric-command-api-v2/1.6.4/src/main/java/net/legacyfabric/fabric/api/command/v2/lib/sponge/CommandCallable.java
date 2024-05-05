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

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;
import net.minecraft.world.World;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

/**
 * A low-level interface for commands that can be executed. For almost all use
 * cases, higher-level tools should be used instead, like {@link CommandSpec}.
 *
 * <p>Implementations are not required to implement a sane
 * {@link Object#equals(Object)} but really should.</p>
 */
public interface CommandCallable {
	/**
	 * Execute the command based on input arguments.
	 *
	 * <p>The implementing class must perform the necessary permission
	 * checks.</p>
	 *
	 * @param source    The caller of the command
	 * @param arguments The raw arguments for this command
	 * @return The result of a command being processed
	 * @throws CommandException Thrown on a command error
	 */
	CommandResult process(PermissibleCommandSource source, String arguments) throws CommandException;

	/**
	 * Gets a list of suggestions based on input.
	 *
	 * <p>If a suggestion is chosen by the user, it will replace the last
	 * word.</p>
	 *
	 * @param source         The command source
	 * @param arguments      The arguments entered up to this point
	 * @param targetPosition The position the source is looking at when
	 *                       performing tab completion
	 * @return A list of suggestions
	 * @throws CommandException Thrown if there was a parsing error
	 */
	List<String> getSuggestions(PermissibleCommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException;

	/**
	 * Test whether this command can probably be executed by the given source.
	 *
	 * <p>If implementations are unsure if the command can be executed by
	 * the source, {@code true} should be returned. Return values of this method
	 * may be used to determine whether this command is listed in command
	 * listings.</p>
	 *
	 * @param source The caller of the command
	 * @return Whether permission is (probably) granted
	 */
	boolean testPermission(PermissibleCommandSource source);

	/**
	 * Gets a short one-line description of this command.
	 *
	 * <p>The help system may display the description in the command list.</p>
	 *
	 * @param source The source of the help request
	 * @return A description
	 */
	Optional<ChatMessage> getShortDescription(PermissibleCommandSource source);

	/**
	 * Gets a longer formatted help message about this command.
	 *
	 * <p>It is recommended to use the default text color and style. Sections
	 * with text actions (e.g. hyperlinks) should be underlined.</p>
	 *
	 * <p>Multi-line messages can be created by separating the lines with
	 * {@code \n}.</p>
	 *
	 * <p>The help system may display this message when a source requests
	 * detailed information about a command.</p>
	 *
	 * @param source The source of the help request
	 * @return A help text
	 */
	Optional<ChatMessage> getHelp(PermissibleCommandSource source);

	/**
	 * Gets the usage string of this command.
	 *
	 * <p>A usage string may look like
	 * {@code [-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;}.</p>
	 *
	 * <p>The string must not contain the command alias.</p>
	 *
	 * @param source The source of the help request
	 * @return A usage string
	 */
	ChatMessage getUsage(PermissibleCommandSource source);
}
