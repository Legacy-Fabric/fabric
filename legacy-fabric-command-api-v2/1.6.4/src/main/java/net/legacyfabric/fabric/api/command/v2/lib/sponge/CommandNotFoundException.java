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

import com.google.common.base.Preconditions;

import net.minecraft.text.ChatMessage;

/**
 * This exception is thrown when a sender tries to execute a command that does
 * not exist.
 */
public class CommandNotFoundException extends CommandException {
	private static final long serialVersionUID = -7714518367616848051L;

	private final String command;

	/**
	 * Create an exception with the default message.
	 *
	 * @param command The command that was queried for
	 */
	public CommandNotFoundException(String command) {
		this(ChatMessage.createTextMessage("No such command"), command);
	}

	/**
	 * Create an exception with a custom message.
	 *
	 * @param message The message
	 * @param command The command that was queried for
	 */
	public CommandNotFoundException(ChatMessage message, String command) {
		super(message);
		this.command = Preconditions.checkNotNull(command, "command");
	}

	/**
	 * Returns the command that was queried for.
	 *
	 * @return The command
	 */
	public String getCommand() {
		return this.command;
	}
}
