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

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class OptionalCommandElement extends CommandElement {
	private final CommandElement element;
	@Nullable
	private final Object value;
	private final boolean considerInvalidFormatEmpty;

	public OptionalCommandElement(CommandElement element, @Nullable Object value, boolean considerInvalidFormatEmpty) {
		super(null);
		this.element = element;
		this.value = value;
		this.considerInvalidFormatEmpty = considerInvalidFormatEmpty;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		if (!args.hasNext()) {
			Text key = this.element.getKey();

			if (key != null && this.value != null) {
				context.putArg(key.asString(), this.value);
			}

			return;
		}

		CommandArgs.Snapshot startState = args.getSnapshot();

		try {
			this.element.parse(source, args, context);
		} catch (ArgumentParseException ex) {
			if (this.considerInvalidFormatEmpty || args.hasNext()) { // If there are more args, suppress. Otherwise, throw the error
				args.applySnapshot(startState);

				if (this.element.getKey() != null && this.value != null) {
					context.putArg(this.element.getUntranslatedKey(), this.value);
				}
			} else {
				throw ex;
			}
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return args.hasNext() ? null : this.element.parseValue(source, args);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		return this.element.complete(src, args, context);
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		final Text containingUsage = this.element.getUsage(src);

		if (containingUsage.getString().isEmpty()) {
			return new LiteralText("");
		}

		return new LiteralText("[" + this.element.getUsage(src) + "]");
	}
}
