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

import java.util.Collections;
import java.util.List;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class AllOfCommandElement extends CommandElement {
	private final CommandElement element;

	public AllOfCommandElement(CommandElement element) {
		super(null);
		this.element = element;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		while (args.hasNext()) {
			this.element.parse(source, args, context);
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		CommandArgs.Snapshot startState = null;

		try {
			while (args.hasNext()) {
				startState = args.getSnapshot();
				this.element.parse(src, args, context);
			}
		} catch (ArgumentParseException e) {
			// ignored
		}

		// The final element, if an exception was not thrown, might have more completions available to it.
		// Therefore, we reapply the last snapshot and complete from it.
		if (startState != null) {
			args.applySnapshot(startState);
		}

		if (args.canComplete()) {
			return this.element.complete(src, args, context);
		}

		// If we have more elements, do not complete.
		return Collections.emptyList();
	}

	@Override
	public Text getUsage(PermissibleCommandSource context) {
		return new LiteralText(this.element.getUsage(context).asUnformattedString() + CommandMessageFormatting.STAR_TEXT.computeValue());
	}
}
