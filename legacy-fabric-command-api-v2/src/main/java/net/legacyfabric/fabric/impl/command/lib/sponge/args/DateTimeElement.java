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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class DateTimeElement extends CommandElement {
	private final boolean returnNow;

	public DateTimeElement(Text key, boolean returnNow) {
		super(key);
		this.returnNow = returnNow;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (!args.hasNext() && this.returnNow) {
			return LocalDateTime.now();
		}

		CommandArgs.Snapshot state = args.getSnapshot();
		String date = args.next();

		try {
			return LocalDateTime.parse(date);
		} catch (DateTimeParseException ex) {
			try {
				return LocalDateTime.of(LocalDate.now(), LocalTime.parse(date));
			} catch (DateTimeParseException ex2) {
				try {
					return LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT);
				} catch (DateTimeParseException ex3) {
					if (this.returnNow) {
						args.applySnapshot(state);
						return LocalDateTime.now();
					}

					throw args.createError(new LiteralText("Invalid date-time!"));
				}
			}
		}
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		String date = LocalDateTime.now().withNano(0).toString();

		if (date.startsWith(args.nextIfPresent().orElse(""))) {
			return ImmutableList.of(date);
		} else {
			return ImmutableList.of();
		}
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		if (!this.returnNow) {
			return super.getUsage(src);
		} else {
			return new LiteralText("[" + this.getKey().getString() + "]");
		}
	}
}
