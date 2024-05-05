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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

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
			ChatMessage key = this.getKey();
			throw args.createError(ChatMessage.createTextMessage(String.format("You do not have permission to use the %s argument", key != null ? key : "unknown")));
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
	public ChatMessage getUsage(PermissibleCommandSource src) {
		if (this.isOptional && !src.hasPermission(this.permission)) {
			return ChatMessage.createTextMessage("");
		}

		return this.element.getUsage(src);
	}
}
