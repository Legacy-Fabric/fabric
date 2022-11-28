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

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.KeyElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class RemainingJoinedStringsCommandElement extends KeyElement {
	private final boolean raw;

	public RemainingJoinedStringsCommandElement(Text key, boolean raw) {
		super(key);
		this.raw = raw;
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (this.raw) {
			args.next();
			String ret = args.getRaw().substring(args.getRawPosition());

			while (args.hasNext()) { // Consume remaining args
				args.next();
			}

			return ret;
		}

		final StringBuilder ret = new StringBuilder(args.next());

		while (args.hasNext()) {
			ret.append(' ').append(args.next());
		}

		return ret.toString();
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		return new LiteralText("").append(CommandMessageFormatting.LT_TEXT.asUnformattedString()).append(this.getKey()).append(CommandMessageFormatting.ELLIPSIS_TEXT).append(CommandMessageFormatting.GT_TEXT);
	}
}
