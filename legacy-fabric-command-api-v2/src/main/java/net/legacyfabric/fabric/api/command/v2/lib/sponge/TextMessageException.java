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
		return message == null ? null : message.asString();
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
