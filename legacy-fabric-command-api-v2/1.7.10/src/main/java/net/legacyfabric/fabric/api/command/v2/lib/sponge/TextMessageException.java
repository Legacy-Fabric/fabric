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
 * A subclass of Exception that contains a rich message that is an instance of
 * {@link Text} rather than a String. This allows formatted and localized
 * exception messages.
 */
public class TextMessageException extends Exception {
	private static final long serialVersionUID = -5281221645176698853L;

	private final Text message;

	/**
	 * Constructs a new {@link TextMessageException}.
	 */
	public TextMessageException() {
		this.message = null;
	}

	/**
	 * Constructs a new {@link TextMessageException} with the given message.
	 *
	 * @param message The detail message
	 */
	public TextMessageException(Text message) {
		this.message = message;
	}

	/**
	 * Constructs a new {@link TextMessageException} with the given message and
	 * cause.
	 *
	 * @param message   The detail message
	 * @param throwable The cause
	 */
	public TextMessageException(Text message, Throwable throwable) {
		super(throwable);
		this.message = message;
	}

	/**
	 * Constructs a new {@link TextMessageException} with the given cause.
	 *
	 * @param throwable The cause
	 */
	public TextMessageException(Throwable throwable) {
		super(throwable);
		this.message = null;
	}

	@Override
	public String getMessage() {
		Text message = this.getText();
		return message == null ? null : message.computeValue();
	}

	/**
	 * Returns the text message for this exception, or null if nothing is
	 * present.
	 *
	 * @return The text for this message
	 */
	public Text getText() {
		return this.message;
	}

	@Override
	public String getLocalizedMessage() {
		return this.getMessage();
	}
}
