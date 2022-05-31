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

package net.legacyfabric.fabric.api.command.v2.lib.sponge;

import java.util.Set;

/**
 * Provides information about a mapping between a command and its aliases.
 *
 * <p>Implementations are not required to implement a sane
 * {@link Object#equals(Object)} but may choose to do so.</p>
 */
public interface CommandMapping {
	/**
	 * Gets the primary alias.
	 *
	 * @return The primary alias
	 */
	String getPrimaryAlias();

	/**
	 * Gets an immutable list of all aliases.
	 *
	 * <p>The returned list must contain at least one entry, of which one must
	 * be the one returned by {@link #getPrimaryAlias()}.</p>
	 *
	 * <p>There may be several versions of the same alias with different
	 * casing, although generally implementations should ignore the casing
	 * of aliases.</p>
	 *
	 * @return A set of aliases
	 */
	Set<String> getAllAliases();

	/**
	 * Gets the callable.
	 *
	 * @return The callable
	 */
	CommandCallable getCallable();
}
