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

package io.github.legacyrewoven.impl.command.lib.sponge.args;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import io.github.legacyrewoven.api.command.v2.lib.sponge.args.ArgumentParseException;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandArgs;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.KeyElement;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class UrlElement extends KeyElement {
	public UrlElement(Text key) {
		super(key);
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		String str = args.next();
		URL url;

		try {
			url = new URL(str);
		} catch (MalformedURLException ex) {
			throw new ArgumentParseException(new LiteralText("Invalid URL!"), ex, str, 0);
		}

		try {
			url.toURI();
		} catch (URISyntaxException ex) {
			throw new ArgumentParseException(new LiteralText("Invalid URL!"), ex, str, 0);
		}

		return url;
	}
}
