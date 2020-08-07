/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.event.lifecycle.v1;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ServerEntityEvents {
	private ServerEntityEvents() {
	}

	/**
	 * Called when an Entity is loaded into a ServerWorld.
	 *
	 * <p>When this event is called, the entity is already in the world.
	 *
	 * <p>Note there is no corresponding unload event because entity unloads cannot be reliably tracked.
	 */
	public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> (entity, world) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = world.profiler;
			profiler.push("fabricServerEntityLoad");

			for (Load callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onLoad(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Load callback : callbacks) {
				callback.onLoad(entity, world);
			}
		}
	});

	/**
	 * Called when Lightning strikes in a world.
	 */
	public static final Event<LightningStrike> LIGHTNING_STRIKE = EventFactory.createArrayBacked(LightningStrike.class, (listeners) -> (entity, world, x, y, z) -> {
		for (LightningStrike callback : listeners) {
			callback.onLightningStrike(entity, world, x, y, z);
		}
	});

	/**
	 * Called when an entity is hurt.
	 */
	public static final Event<EntityHurt> HURT = EventFactory.createArrayBacked(EntityHurt.class, (listeners) -> (entity, source, original, damage, attacker) -> {
		for (EntityHurt callback : listeners) {
			callback.entityHurt(entity, source, original, damage, attacker);
		}
	});

	/**
	 * Called when an entity is killed.
	 */
	public static final Event<EntityKilled> KILLED = EventFactory.createArrayBacked(EntityKilled.class, (listeners) -> (entity) -> {
		for (EntityKilled callback : listeners) {
			callback.entityKilled(entity);
		}
	});

	@FunctionalInterface
	public interface Load {
		void onLoad(Entity entity, ServerWorld world);
	}

	@FunctionalInterface
	public interface LightningStrike {
		void onLightningStrike(LightningBoltEntity entity, World world, double x, double y, double z);
	}

	@FunctionalInterface
	public interface EntityHurt {
		void entityHurt(LivingEntity entity, DamageSource damageSource, float originalHealth, float damage, @Nullable Entity attacker);
	}

	@FunctionalInterface
	public interface EntityKilled {
		void entityKilled(Entity killed);
	}
}
