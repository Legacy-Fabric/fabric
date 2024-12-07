/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
