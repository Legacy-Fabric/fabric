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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

@FunctionalInterface
public interface Disambiguator {
	/**
	 * Disambiguate an alias in cases where there are multiple command mappings
	 * registered for a given alias.
	 *
	 * @param source           The PermissibleCommandSource executing the command, if any
	 * @param aliasUsed        The alias input by the user
	 * @param availableOptions The commands registered to this alias
	 * @return The specific command to use
	 */
	Optional<CommandMapping> disambiguate(@Nullable PermissibleCommandSource source, String aliasUsed, List<CommandMapping> availableOptions);
}
