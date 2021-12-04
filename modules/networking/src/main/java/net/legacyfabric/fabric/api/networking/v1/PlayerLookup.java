/*
 * Copyright (c) 2021 Legacy Rewoven
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.api.networking.v1;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import net.legacyfabric.fabric.mixin.networking.EntityTrackerAccessor;
import net.legacyfabric.fabric.mixin.networking.EntityTrackerEntryAccessor;

/**
 * For example, a block entity may use the methods in this class to send a packet to all clients which can see the block entity in order notify clients about a change.
 *
 * <p>The word "tracking" means that an entity/chunk on the server is known to a player's client (within in view distance) and the (block) entity should notify tracking clients of changes.
 *
 * <p>These methods should only be called on the server thread and only be used on logical a server.
 */
public final class PlayerLookup {
	/**
	 * Gets all the players on the minecraft server.
	 *
	 * <p>The returned collection is immutable.
	 *
	 * @param server the server
	 * @return all players on the server
	 */
	public static Collection<ServerPlayerEntity> all(MinecraftServer server) {
		Objects.requireNonNull(server, "The server cannot be null");

		// return an immutable collection to guard against accidental removals.
		if (server.getPlayerManager() != null) {
			return Collections.unmodifiableCollection(server.getPlayerManager().getPlayers());
		}

		return Collections.emptyList();
	}

	/**
	 * Gets all the players in a server world.
	 *
	 * <p>The returned collection is immutable.
	 *
	 * @param world the server world
	 * @return the players in the server world
	 */
	public static Collection<ServerPlayerEntity> world(ServerWorld world) {
		Objects.requireNonNull(world, "The world cannot be null");

		// return an immutable collection to guard against accidental removals.
		return Collections.unmodifiableCollection(world.getServer().getPlayerManager().getPlayers());
	}

	/**
	 * Gets all players tracking an entity in a server world.
	 *
	 * <p>The returned collection is immutable.
	 *
	 * <p><b>Warning</b>: If the provided entity is a player, it is not
	 * guaranteed by the contract that said player is included in the
	 * resulting stream.
	 *
	 * @param entity the entity being tracked
	 * @return the players tracking the entity
	 * @throws IllegalArgumentException if the entity is not in a server world
	 */
	public static Collection<ServerPlayerEntity> tracking(Entity entity) {
		Objects.requireNonNull(entity, "Entity cannot be null");

		if (entity.world instanceof ServerWorld) {
			return Optional.of(entity.world)
					.map(ServerWorld.class::cast)
					.map(ServerWorld::getEntityTracker)
					.map(EntityTrackerAccessor.class::cast)
					.map(EntityTrackerAccessor::getTrackedEntityIds)
					.map(c -> c.get(entity.getEntityId()))
					.map(EntityTrackerEntryAccessor.class::cast)
					.map(EntityTrackerEntryAccessor::getPlayers)
					.map(Collections::unmodifiableSet)
					.orElseGet(Collections::emptySet);
		}

		throw new IllegalArgumentException("Only supported on server worlds!");
	}

	/**
	 * Gets all players around a position in a world.
	 *
	 * <p>The distance check is done in the three-dimensional space instead of in the horizontal plane.
	 *
	 * @param world  the world
	 * @param pos    the position
	 * @param radius the maximum distance from the position in blocks
	 * @return the players around the position
	 */
	public static Collection<ServerPlayerEntity> around(ServerWorld world, Vec3d pos, double radius) {
		double radiusSq = radius * radius;

		return world(world)
				.stream()
				.filter((p) -> p.getDistanceTo(pos.x, pos.y, pos.z) <= radiusSq)
				.collect(Collectors.toList());
	}

	/**
	 * Gets all players around a position in a world.
	 *
	 * <p>The distance check is done in the three-dimensional space instead of in the horizontal plane.
	 *
	 * @param world  the world
	 * @param pos    the position (can be a block pos)
	 * @param radius the maximum distance from the position in blocks
	 * @return the players around the position
	 */
	public static Collection<ServerPlayerEntity> around(ServerWorld world, Vec3i pos, double radius) {
		double radiusSq = radius * radius;

		return world(world)
				.stream()
				.filter((p) -> p.getDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= radiusSq)
				.collect(Collectors.toList());
	}

	private PlayerLookup() {
	}
}
