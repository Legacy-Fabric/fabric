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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class WithSuggestionsElement extends CommandElement {
	private final CommandElement wrapped;
	private final Function<PermissibleCommandSource, Iterable<String>> suggestions;
	private final boolean requireBegin;

	public WithSuggestionsElement(CommandElement wrapped, Function<PermissibleCommandSource, Iterable<String>> suggestions, boolean requireBegin) {
		super(wrapped.getKey());
		this.wrapped = wrapped;
		this.suggestions = suggestions;
		this.requireBegin = requireBegin;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
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
