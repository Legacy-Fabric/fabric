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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;

class TokenizerState {
	private final boolean lenient;
	private final String buffer;
	private int index = -1;

	TokenizerState(String buffer, boolean lenient) {
		this.buffer = buffer;
		this.lenient = lenient;
	}

	// Utility methods
	public boolean hasMore() {
		return this.index + 1 < this.buffer.length();
	}

	public int peek() throws ArgumentParseException {
		if (!this.hasMore()) {
			throw this.createException(new LiteralText("Buffer overrun while parsing args"));
		}

		return this.buffer.codePointAt(this.index + 1);
	}

	public int next() throws ArgumentParseException {
		if (!this.hasMore()) {
			throw this.createException(new LiteralText("Buffer overrun while parsing args"));
		}

		return this.buffer.codePointAt(++this.index);
	}

	public ArgumentParseException createException(Text message) {
		return new ArgumentParseException(message, this.buffer, this.index);
	}

	public boolean isLenient() {
		return this.lenient;
	}

	public int getIndex() {
		return this.index;
	}
}
