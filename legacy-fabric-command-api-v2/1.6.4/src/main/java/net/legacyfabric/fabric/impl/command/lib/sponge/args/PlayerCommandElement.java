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

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.SelectorCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class PlayerCommandElement extends SelectorCommandElement {
	private final boolean returnSource;

	public PlayerCommandElement(ChatMessage key, boolean returnSource) {
		super(key);
		this.returnSource = returnSource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (!args.hasNext() && this.returnSource) {
			return this.tryReturnSource(source, args);
		}

		CommandArgs.Snapshot state = args.getSnapshot();

		try {
			return StreamSupport.stream(((Iterable<Entity>) super.parseValue(source, args)).spliterator(), false).filter(e -> e instanceof PlayerEntity).collect(Collectors.toList());
		} catch (ArgumentParseException ex) {
			if (this.returnSource) {
				args.applySnapshot(state);
				return this.tryReturnSource(source, args);
			}

			throw ex;
		}
	}

	@Override
	protected Iterable<String> getChoices(PermissibleCommandSource source) {
		return (Iterable<String>) MinecraftServer.getServer().getPlayerManager().players.stream().map(player -> ((PlayerEntity) player).getUsername()).collect(Collectors.toSet());
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		Optional<PlayerEntity> ret = MinecraftServer.getServer().getPlayerManager().players.stream().findFirst().map(Function.identity());

		if (!ret.isPresent()) {
			throw new IllegalArgumentException("Input value " + choice + " was not a player");
		}

		return ret.get();
	}

	private PlayerEntity tryReturnSource(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (source instanceof PlayerEntity) {
			return ((PlayerEntity) source);
		} else {
			throw args.createError(ChatMessage.createTextMessage("No players matched and source was not a player!"));
		}
	}

	@Override
	public ChatMessage getUsage(PermissibleCommandSource src) {
		return src != null && this.returnSource ? ChatMessage.createTextMessage("[" + super.getUsage(src) + "]") : super.getUsage(src);
	}
}
