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
