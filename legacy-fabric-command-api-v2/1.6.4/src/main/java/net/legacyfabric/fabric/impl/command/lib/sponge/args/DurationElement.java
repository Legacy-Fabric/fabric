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

import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.KeyElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class DurationElement extends KeyElement {
	public DurationElement(ChatMessage key) {
		super(key);
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		String s = args.next().toUpperCase();

		if (!s.contains("T")) {
			if (s.contains("D")) {
				if (s.contains("H") || s.contains("M") || s.contains("S")) {
					s = s.replace("D", "DT");
				}
			} else {
				if (s.startsWith("P")) {
					s = "PT" + s.substring(1);
				} else {
					s = "T" + s;
				}
			}
		}

		if (!s.startsWith("P")) {
			s = "P" + s;
		}

		try {
			return Duration.parse(s);
		} catch (DateTimeParseException ex) {
			throw args.createError(ChatMessage.createTextMessage("Invalid duration!"));
		}
	}
}
