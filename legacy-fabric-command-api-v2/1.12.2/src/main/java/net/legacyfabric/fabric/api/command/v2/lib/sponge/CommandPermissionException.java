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

/**
 * This exception is thrown when a subject does not have permission to execute
 * a command.
 */
public class CommandPermissionException extends CommandException {
	private static final long serialVersionUID = -6057386975881181213L;

	/**
	 * Create an exception with the default message.
	 */
	public CommandPermissionException() {
		this(new LiteralText("You do not have permission to use this command!"));
	}

	/**
	 * Create a permissions exception with a custom message.
	 *
	 * @param message The message
	 */
	public CommandPermissionException(Text message) {
		super(message);
	}

	/**
	 * Create a permissions exception with a custom message and cause.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public CommandPermissionException(Text message, Throwable cause) {
		super(message, cause);
	}
}
