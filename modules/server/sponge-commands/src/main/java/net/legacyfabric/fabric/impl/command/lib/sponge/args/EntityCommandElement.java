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

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.SelectorCommandElement;
import net.legacyfabric.fabric.impl.command.CommandInitializer;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class EntityCommandElement extends SelectorCommandElement {
	private final boolean returnTarget;
	private final boolean returnSource;
	@Nullable
	private final Class<? extends Entity> clazz;

	public EntityCommandElement(Text key, boolean returnSource, boolean returnTarget, @Nullable Class<? extends Entity> clazz) {
		super(key);
		this.returnSource = returnSource;
		this.returnTarget = returnTarget;
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		if (!args.hasNext()) {
			if (this.returnSource) {
				return this.tryReturnSource(source, args, true);
			}

			if (this.returnTarget) {
				return this.tryReturnTarget(source, args);
			}
		}

		CommandArgs.Snapshot state = args.getSnapshot();

		try {
			Iterable<Entity> entities = (Iterable<Entity>) super.parseValue(source, args);

			for (Entity entity : entities) {
				if (!this.checkEntity(entity)) {
					Text name = new LiteralText(this.clazz == null ? "null" : this.clazz.getSimpleName());
					throw args.createError(new LiteralText("The entity is not of the required type! (").append(name).append(")"));
				}
			}

			return entities;
		} catch (ArgumentParseException ex) {
			if (this.returnSource) {
				args.applySnapshot(state);
				return this.tryReturnSource(source, args, true);
			}

			throw ex;
		}
	}

	@Override
	protected Iterable<String> getChoices(PermissibleCommandSource source) {
		Set<String> worldEntities = Arrays.stream(source.method_12833().worlds).flatMap(world -> world.entities.stream())
				.filter(this::checkEntity)
				.map(entity -> entity.getUuid().toString()).collect(Collectors.toSet());
		Collection<PlayerEntity> players = Sets.newHashSet(source.method_12833().getPlayerManager().getPlayers());

		if (!players.isEmpty() && this.checkEntity(players.iterator().next())) {
			final Set<String> setToReturn = Sets.newHashSet(worldEntities); // to ensure mutability
			players.forEach(x -> setToReturn.add(x.getTranslationKey()));
			return setToReturn;
		}

		return worldEntities;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		UUID uuid;

		try {
			uuid = UUID.fromString(choice);
		} catch (IllegalArgumentException ignored) {
			// Player could be a name
			return Optional.ofNullable(CommandInitializer.getServerInstance.getPlayerManager().getPlayer(choice)).orElseThrow(() -> new IllegalArgumentException("Input value " + choice + " does not represent a valid entity"));
		}

		boolean found = false;
		Optional<Entity> ret = Optional.ofNullable(CommandInitializer.getServerInstance.getEntity(uuid));

		if (ret.isPresent()) {
			Entity entity = ret.get();

			if (this.checkEntity(entity)) {
				return entity;
			}

			found = true;
		}

		if (found) {
			throw new IllegalArgumentException("Input value " + choice + " was not an entity of the required type!");
		}

		throw new IllegalArgumentException("Input value " + choice + " was not an entity");
	}

	private Entity tryReturnSource(PermissibleCommandSource source, CommandArgs args, boolean check) throws ArgumentParseException {
		if (source instanceof Entity && (!check || this.checkEntity((Entity) source))) {
			return (Entity) source;
		}

		throw args.createError(new LiteralText("No entities matched and source was not an entity!"));
	}

	// TODO
	private Entity tryReturnTarget(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		Entity entity = this.tryReturnSource(source, args, false);
		throw args.createError(new LiteralText("No entities matched and source was not looking at a valid entity!"));
		// return entity.getWorld().getIntersectingEntities(entity, 10).stream().filter(e -> !e.getEntity().equals(entity)).map(EntityUniverse.EntityHit::getEntity).filter(this::checkEntity).findFirst().orElseThrow(() -> args.createError(new LiteralText("No entities matched and source was not looking at a valid entity!")));
	}

	private boolean checkEntity(Entity entity) {
		if (this.clazz == null) {
			return true;
		} else {
			return this.clazz.isAssignableFrom(entity.getClass());
		}
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		return src instanceof Entity && (this.returnSource || this.returnTarget) ? new LiteralText("[" + this.getKey().getString() + "]") : super.getUsage(src);
	}
}
