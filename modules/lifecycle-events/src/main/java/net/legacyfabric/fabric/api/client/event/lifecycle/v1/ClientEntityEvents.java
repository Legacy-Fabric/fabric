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

package net.legacyfabric.fabric.api.client.event.lifecycle.v1;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.profiler.Profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public final class ClientEntityEvents {
	public ClientEntityEvents() {
	}

	/**
	 * Called when an Entity is loaded into a ClientWorld.
	 *
	 * <p>When this event is called, the chunk is already in the world.
	 */
	public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> (entity, world) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = world.profiler;
			profiler.push("fabricClientEntityLoad");

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
	 * Called when an Entity is about to be unloaded from a ClientWorld.
	 *
	 * <p>When this event is called, the entity is still present in the world.
	 */
	public static final Event<Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> (entity, world) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = world.profiler;
			profiler.push("fabricClientEntityLoad");

			for (Unload callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onUnload(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Unload callback : callbacks) {
				callback.onUnload(entity, world);
			}
		}
	});

	@FunctionalInterface
	public interface Load {
		void onLoad(Entity entity, ClientWorld world);
	}

	@FunctionalInterface
	public interface Unload {
		void onUnload(Entity entity, ClientWorld world);
	}
}
