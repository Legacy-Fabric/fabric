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

import net.minecraft.text.Text;

/**
 * Thrown when an executed command raises an error or when execution of
 * the command failed.
 */
public class CommandException extends TextMessageException {
	private static final long serialVersionUID = 4626722485860074825L;

	private final boolean includeUsage;

	/**
	 * Constructs a new {@link CommandException} with the given message.
	 *
	 * @param message The detail message
	 */
	public CommandException(Text message) {
		this(message, false);
	}

	/**
	 * Constructs a new {@link CommandException} with the given message and
	 * the given cause.
	 *
	 * @param message The detail message
	 * @param cause   The cause
	 */
	public CommandException(Text message, Throwable cause) {
		this(message, cause, false);
	}

	/**
	 * Constructs a new {@link CommandException} with the given message.
	 *
	 * @param message      The detail message
	 * @param includeUsage Whether to include usage in the exception
	 */
	public CommandException(Text message, boolean includeUsage) {
		super(message);
		this.includeUsage = includeUsage;
	}

	/**
	 * Constructs a new {@link CommandException} with the given message and
	 * the given cause.
	 *
	 * @param message      The detail message
	 * @param cause        The cause
	 * @param includeUsage Whether to include the usage in the exception
	 */
	public CommandException(Text message, Throwable cause, boolean includeUsage) {
		super(message, cause);
		this.includeUsage = includeUsage;
	}

	/**
	 * Gets whether the exception should include usage in
	 * the presentation of the exception/stack-trace.
	 *
	 * @return Whether to include usage in the exception
	 */
	public boolean shouldIncludeUsage() {
		return this.includeUsage;
	}
}
