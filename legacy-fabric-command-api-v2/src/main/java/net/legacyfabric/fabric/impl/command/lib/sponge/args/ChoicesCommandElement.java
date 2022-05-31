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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.TriState;

public class ChoicesCommandElement extends CommandElement {
	public static final int CUTOFF = 5;
	private final Supplier<Collection<String>> keySupplier;
	private final Function<String, ?> valueSupplier;
	private final TriState choicesInUsage;

	public ChoicesCommandElement(Text key, Supplier<Collection<String>> keySupplier, Function<String, ?> valueSupplier, TriState choicesInUsage) {
		super(key);
		this.keySupplier = keySupplier;
		this.valueSupplier = valueSupplier;
		this.choicesInUsage = choicesInUsage;
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		Object value = this.valueSupplier.apply(args.next());

		if (value == null) {
			throw args.createError(new LiteralText(String.format("Argument was not a valid choice. Valid choices: %s", this.keySupplier.get().toString())));
		}

		return value;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		final String prefix = args.nextIfPresent().orElse("");
		return Collections.unmodifiableList(this.keySupplier.get().stream().filter((input) -> input.startsWith(prefix)).collect(Collectors.toList()));
	}

	@Override
	public Text getUsage(PermissibleCommandSource commander) {
		Collection<String> keys = this.keySupplier.get();

		if (this.choicesInUsage == TriState.TRUE || (this.choicesInUsage == TriState.DEFAULT && keys.size() <= CUTOFF)) {
			final Text build = new LiteralText("");
			build.append(CommandMessageFormatting.LT_TEXT);

			for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
				build.append(new LiteralText(it.next()));

				if (it.hasNext()) {
					build.append(CommandMessageFormatting.PIPE_TEXT);
				}
			}

			build.append(CommandMessageFormatting.GT_TEXT);
			return new LiteralText(build.getString());
		}

		return super.getUsage(commander);
	}
}
