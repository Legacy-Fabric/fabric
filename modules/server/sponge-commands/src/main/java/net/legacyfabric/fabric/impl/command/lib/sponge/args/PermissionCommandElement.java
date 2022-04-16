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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class PermissionCommandElement extends CommandElement {
	private final CommandElement element;
	private final String permission;
	private final boolean isOptional;

	public PermissionCommandElement(CommandElement element, String permission, boolean isOptional) {
		super(element.getKey());
		this.element = element;
		this.permission = permission;
		this.isOptional = isOptional;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (this.checkPermission(source, args)) {
			return this.element.parseValue(source, args);
		}

		return null;
	}

	private boolean checkPermission(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		boolean hasPermission = source.hasPermission(this.permission);

		if (!hasPermission && !this.isOptional) {
			Text key = this.getKey();
			throw args.createError(new LiteralText(String.format("You do not have permission to use the %s argument", key != null ? key : "unknown")));
		}

		return hasPermission;
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		boolean flag = /*src.hasPermission(this.permission)*/ true;

		if (!flag) {
			return ImmutableList.of();
		}

		return this.element.complete(src, args, context);
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		if (this.checkPermission(source, args)) {
			this.element.parse(source, args, context);
		}
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		if (this.isOptional && !src.hasPermission(this.permission)) {
			return new LiteralText("");
		}

		return this.element.getUsage(src);
	}
}
