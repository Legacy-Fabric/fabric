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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * An immutable command mapping instance that returns the same objects that
 * this instance is constructed with.
 */
public final class ImmutableCommandMapping implements CommandMapping {
	private final String primary;
	private final Set<String> aliases;
	private final CommandCallable callable;

	/**
	 * Create a new instance.
	 *
	 * @param callable The command callable
	 * @param primary  The primary alias
	 * @param alias    A list of all aliases
	 * @throws IllegalArgumentException Thrown if aliases are duplicated
	 */
	public ImmutableCommandMapping(CommandCallable callable, String primary, String... alias) {
		this(callable, primary, Arrays.asList(Preconditions.checkNotNull(alias, "alias")));
	}

	/**
	 * Create a new instance.
	 *
	 * @param callable The command callable
	 * @param primary  The primary alias
	 * @param aliases  A collection of all aliases
	 * @throws IllegalArgumentException Thrown if aliases are duplicated
	 */
	public ImmutableCommandMapping(CommandCallable callable, String primary, Collection<String> aliases) {
		Preconditions.checkNotNull(primary, "primary");
		Preconditions.checkNotNull(aliases, "aliases");
		this.primary = primary;
		this.aliases = new HashSet<>(aliases);
		this.aliases.add(primary);
		this.callable = Preconditions.checkNotNull(callable, "callable");
	}

	@Override
	public String getPrimaryAlias() {
		return this.primary;
	}

	@Override
	public Set<String> getAllAliases() {
		return ImmutableSet.copyOf(this.aliases);
	}

	@Override
	public CommandCallable getCallable() {
		return this.callable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		ImmutableCommandMapping that = (ImmutableCommandMapping) o;
		return this.primary.equals(that.primary) && this.aliases.equals(that.aliases) && this.callable.equals(that.callable);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.primary, this.aliases, this.callable);
	}

	@Override
	public String toString() {
		return "ImmutableCommandMapping{"
				+ "primary='" + this.primary + '\''
				+ ", aliases=" + this.aliases
				+ ", spec=" + this.callable
				+ '}';
	}
}
