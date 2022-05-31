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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.Selector;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public abstract class SelectorCommandElement extends PatternMatchingCommandElement {
	protected SelectorCommandElement(@Nullable Text key) {
		super(key);
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		String arg = args.peek();

		if (arg.startsWith("@")) { // Possibly a selector
			try {
				return Selector.parse(args.next()).resolve(source);
			} catch (IllegalArgumentException ex) {
				throw args.createError(new LiteralText(ex.getMessage()));
			}
		}

		return super.parseValue(source, args);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		CommandArgs.Snapshot state = args.getSnapshot();
		final Optional<String> nextArg = args.nextIfPresent();
		args.applySnapshot(state);
		List<String> choices = nextArg.isPresent() ? Selector.complete(nextArg.get()) : ImmutableList.of();

		if (choices.isEmpty()) {
			choices = super.complete(src, args, context);
		}

		return choices;
	}
}
