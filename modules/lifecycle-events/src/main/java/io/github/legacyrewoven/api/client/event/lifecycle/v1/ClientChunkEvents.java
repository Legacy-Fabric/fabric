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

package io.github.legacyrewoven.api.client.event.lifecycle.v1;

import io.github.legacyrewoven.api.event.Event;
import io.github.legacyrewoven.api.event.EventFactory;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.chunk.Chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ClientChunkEvents {
	private ClientChunkEvents() {
	}

	/**
	 * Called when a chunk is loaded into a ClientWorld.
	 *
	 * <p>When this event is called, the chunk is already in the world.
	 */
	public static final Event<Load> CHUNK_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> (clientWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = clientWorld.profiler;
			profiler.push("fabricClientChunkLoad");

			for (Load callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkLoad(clientWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Load callback : callbacks) {
				callback.onChunkLoad(clientWorld, chunk);
			}
		}
	});

	/**
	 * Called when a chunk is about to be unloaded from a ClientWorld.
	 *
	 * <p>When this event is called, the chunk is still present in the world.
	 */
	public static final Event<Unload> CHUNK_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> (clientWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = clientWorld.profiler;
			profiler.push("fabricClientChunkUnload");

			for (Unload callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkUnload(clientWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (Unload callback : callbacks) {
				callback.onChunkUnload(clientWorld, chunk);
			}
		}
	});

	@FunctionalInterface
	public interface Load {
		void onChunkLoad(ClientWorld world, Chunk chunk);
	}

	@FunctionalInterface
	public interface Unload {
		void onChunkUnload(ClientWorld world, Chunk chunk);
	}
}
