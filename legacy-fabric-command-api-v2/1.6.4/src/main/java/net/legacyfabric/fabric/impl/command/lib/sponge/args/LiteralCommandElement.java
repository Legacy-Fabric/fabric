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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class LiteralCommandElement extends CommandElement {
	private final List<String> expectedArgs;
	@Nullable
	private final Object putValue;

	public LiteralCommandElement(@Nullable ChatMessage key, List<String> expectedArgs, @Nullable Object putValue) {
		super(key);
		this.expectedArgs = ImmutableList.copyOf(expectedArgs);
		this.putValue = putValue;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		for (String arg : this.expectedArgs) {
			String current;

			if (!(current = args.next()).equalsIgnoreCase(arg)) {
				throw args.createError(ChatMessage.createTextMessage(String.format("Argument %s did not match expected next argument %s", current, arg)));
			}
		}

		return this.putValue;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		for (String arg : this.expectedArgs) {
			final Optional<String> next = args.nextIfPresent();

			if (!next.isPresent()) {
				break;
			} else if (args.hasNext()) {
				if (!next.get().equalsIgnoreCase(arg)) {
					break;
				}
			} else {
				if (arg.toLowerCase().startsWith(next.get().toLowerCase())) { // Case-insensitive compare
					return ImmutableList.of(arg); // TODO: Possibly complete all remaining args? Does that even work
				}
			}
		}

		return ImmutableList.of();
	}

	@Override
	public ChatMessage getUsage(PermissibleCommandSource src) {
		return ChatMessage.createTextMessage(Joiner.on(' ').join(this.expectedArgs));
	}
}
