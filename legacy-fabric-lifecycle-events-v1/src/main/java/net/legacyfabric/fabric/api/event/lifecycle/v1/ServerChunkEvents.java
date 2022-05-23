/*
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

package net.legacyfabric.fabric.api.event.lifecycle.v1;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.chunk.Chunk;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

public final class ServerChunkEvents {
	/**
	 * Called when an chunk is loaded into a ServerWorld.
	 *
	 * <p>When this event is called, the chunk is already in the world.
	 */
	public static final Event<Load> CHUNK_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> (serverWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = serverWorld.profiler;
			profiler.push("fabricServerChunkLoad");

			for (Load callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkLoad(serverWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Load callback : callbacks) {
				callback.onChunkLoad(serverWorld, chunk);
			}
		}
	});
	/**
	 * Called when an chunk is unloaded from a ServerWorld.
	 *
	 * <p>When this event is called, the chunk is still present in the world.
	 */
	public static final Event<Unload> CHUNK_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> (serverWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = serverWorld.profiler;
			profiler.push("fabricServerChunkUnload");

			for (Unload callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkUnload(serverWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Unload callback : callbacks) {
				callback.onChunkUnload(serverWorld, chunk);
			}
		}
	});

	private ServerChunkEvents() {
	}

	@FunctionalInterface
	public interface Load {
		void onChunkLoad(ServerWorld world, Chunk chunk);
	}

	@FunctionalInterface
	public interface Unload {
		void onChunkUnload(ServerWorld world, Chunk chunk);
	}
}
