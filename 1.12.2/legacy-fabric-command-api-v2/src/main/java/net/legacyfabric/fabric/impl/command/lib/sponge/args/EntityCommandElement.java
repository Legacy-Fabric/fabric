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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.SelectorCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

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
		Set<String> worldEntities = Arrays.stream(source.getMinecraftServer().worlds).flatMap(world -> world.entities.stream())
				.filter(this::checkEntity)
				.map(entity -> entity.getUuid().toString()).collect(Collectors.toSet());
		Collection<PlayerEntity> players = Sets.newHashSet(source.getMinecraftServer().getPlayerManager().getPlayers());

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
			return Optional.ofNullable(this.getServer().getPlayerManager().getPlayer(choice)).orElseThrow(() -> new IllegalArgumentException("Input value " + choice + " does not represent a valid entity"));
		}

		boolean found = false;
		Optional<Entity> ret = Optional.ofNullable(this.getServer().getEntity(uuid));

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
		return src instanceof Entity && (this.returnSource || this.returnTarget) ? new LiteralText("[" + this.getKey().asUnformattedString() + "]") : super.getUsage(src);
	}
}
