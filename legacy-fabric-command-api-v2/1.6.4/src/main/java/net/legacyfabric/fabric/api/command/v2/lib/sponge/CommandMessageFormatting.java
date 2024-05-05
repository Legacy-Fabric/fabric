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

import net.minecraft.text.ChatMessage;
import net.minecraft.util.Formatting;

public class CommandMessageFormatting {
	private CommandMessageFormatting() {
	}

	public static final ChatMessage PIPE_TEXT = ChatMessage.createTextMessage("|");
	public static final ChatMessage SPACE_TEXT = ChatMessage.createTextMessage(" ");
	public static final ChatMessage STAR_TEXT = ChatMessage.createTextMessage("*");
	public static final ChatMessage LT_TEXT = ChatMessage.createTextMessage("<");
	public static final ChatMessage GT_TEXT = ChatMessage.createTextMessage(">");
	public static final ChatMessage ELLIPSIS_TEXT = ChatMessage.createTextMessage("…");

	/**
	 * Format text to be output as an error directly to a sender. Not necessary
	 * when creating an exception to be thrown
	 *
	 * @param error The error message
	 * @return The formatted error message.
	 */
	public static ChatMessage error(ChatMessage error) {
		return error.setColor(Formatting.RED);
	}

	/**
	 * Format text to be output as a debug message directly to a sender.
	 *
	 * @param debug The debug message
	 * @return The formatted debug message.
	 */
	public static ChatMessage debug(ChatMessage debug) {
		return debug.setColor(Formatting.GRAY);
	}
}
