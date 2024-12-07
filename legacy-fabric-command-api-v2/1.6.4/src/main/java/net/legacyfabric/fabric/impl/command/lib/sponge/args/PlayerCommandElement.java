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

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.SelectorCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.ChatMessage;

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
		Optional<PlayerEntity> ret = this.getServer().getPlayerManager().players.stream().findFirst();

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
		return src != null && this.returnSource ? ChatMessage.createTextMessage("[" + super.getUsage(src).toString() + "]") : super.getUsage(src);
	}
}
