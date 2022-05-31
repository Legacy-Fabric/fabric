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

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class RepeatedCommandElement extends CommandElement {
	private final CommandElement element;
	private final int times;

	public RepeatedCommandElement(CommandElement element, int times) {
		super(null);
		this.element = element;
		this.times = times;
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		for (int i = 0; i < this.times; ++i) {
			this.element.parse(source, args, context);
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		for (int i = 0; i < this.times; ++i) {
			CommandArgs.Snapshot startState = args.getSnapshot();

			try {
				this.element.parse(src, args, context);
			} catch (ArgumentParseException e) {
				args.applySnapshot(startState);
				return this.element.complete(src, args, context);
			}
		}

		return Collections.emptyList();
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		return new LiteralText(this.times + '*' + this.element.getUsage(src).getString());
	}
}
