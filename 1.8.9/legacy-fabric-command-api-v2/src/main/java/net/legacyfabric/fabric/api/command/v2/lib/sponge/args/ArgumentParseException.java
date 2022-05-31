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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args;

import com.google.common.base.Strings;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;

/**
 * Exception thrown when an error occurs while parsing arguments.
 */
public class ArgumentParseException extends CommandException {
	private static final long serialVersionUID = -8555316116315990226L;

	private final String source;
	private final int position;

	/**
	 * Return a new {@link ArgumentParseException} with the given message, source and position.
	 *
	 * @param message  The message to use for this exception
	 * @param source   The source string being parsed
	 * @param position The current position in the source string
	 */
	public ArgumentParseException(Text message, String source, int position) {
		super(message, true);
		this.source = source;
		this.position = position;
	}

	/**
	 * Return a new {@link ArgumentParseException} with the given message, cause, source and position.
	 *
	 * @param message  The message to use for this exception
	 * @param cause    The cause for this exception
	 * @param source   The source string being parsed
	 * @param position The current position in the source string
	 */
	public ArgumentParseException(Text message, Throwable cause, String source, int position) {
		super(message, cause, true);
		this.source = source;
		this.position = position;
	}

	@Override
	public Text getText() {
		Text superText = super.getText();

		if (this.source == null || this.source.isEmpty()) {
			return super.getText();
		} else if (superText == null) {
			return new LiteralText(this.getAnnotatedPosition());
		} else {
			return new LiteralText(superText.getString() + "\n" + this.getAnnotatedPosition());
		}
	}

	private Text getSuperText() {
		return super.getText();
	}

	/**
	 * Return a string pointing to the position of the arguments when this
	 * exception occurs.
	 *
	 * @return The appropriate position string
	 */
	public String getAnnotatedPosition() {
		String source = this.source;
		int position = this.position;

		if (source.length() > 80) {
			if (position >= 37) {
				int startPos = position - 37;
				int endPos = Math.min(source.length(), position + 37);

				if (endPos < source.length()) {
					source = "..." + source.substring(startPos, endPos) + "...";
				} else {
					source = "..." + source.substring(startPos, endPos);
				}

				position -= 40;
			} else {
				source = source.substring(0, 77) + "...";
			}
		}

		return source + "\n" + Strings.repeat(" ", position) + "^";
	}

	/**
	 * Gets the position of the last fetched argument in the provided source
	 * string.
	 *
	 * @return The source string to get position for
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Returns the source string arguments are being parsed from.
	 *
	 * @return The source string
	 */
	public String getSourceString() {
		return this.source;
	}

	/**
	 * An {@link ArgumentParseException} where the usage is already specified.
	 */
	public static class WithUsage extends ArgumentParseException {
		private static final long serialVersionUID = -786214501012293475L;

		private final Text usage;

		public WithUsage(ArgumentParseException wrapped, Text usage) {
			super(wrapped.getSuperText(), wrapped.getCause(), wrapped.getSourceString(), wrapped.getPosition());
			this.usage = usage;
		}

		/**
		 * Gets the usage associated with this exception.
		 *
		 * @return The usage
		 */
		public Text getUsage() {
			return this.usage;
		}
	}
}
