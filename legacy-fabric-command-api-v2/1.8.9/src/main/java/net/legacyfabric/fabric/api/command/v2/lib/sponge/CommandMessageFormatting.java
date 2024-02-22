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

package net.legacyfabric.fabric.api.command.v2.lib.sponge;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandMessageFormatting {
	private CommandMessageFormatting() {
	}

	public static final Text PIPE_TEXT = new LiteralText("|");
	public static final Text SPACE_TEXT = new LiteralText(" ");
	public static final Text STAR_TEXT = new LiteralText("*");
	public static final Text LT_TEXT = new LiteralText("<");
	public static final Text GT_TEXT = new LiteralText(">");
	public static final Text ELLIPSIS_TEXT = new LiteralText("…");

	/**
	 * Format text to be output as an error directly to a sender. Not necessary
	 * when creating an exception to be thrown
	 *
	 * @param error The error message
	 * @return The formatted error message.
	 */
	public static Text error(Text error) {
		return error.setStyle(error.getStyle().setFormatting(Formatting.RED));
	}

	/**
	 * Format text to be output as a debug message directly to a sender.
	 *
	 * @param debug The debug message
	 * @return The formatted debug message.
	 */
	public static Text debug(Text debug) {
		return debug.setStyle(debug.getStyle().setFormatting(Formatting.GRAY));
	}
}
