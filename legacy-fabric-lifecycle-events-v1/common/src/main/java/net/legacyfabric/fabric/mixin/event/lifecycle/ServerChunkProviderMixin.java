/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.event.lifecycle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.storage.ChunkStorage;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerChunkEvents;

@Mixin(ServerChunkCache.class)
public class ServerChunkProviderMixin {
	@Shadow
	private ServerWorld world;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/storage/ChunkStorage;saveChunk(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/WorldChunk;)V"), method = "saveChunk")
	public void api$chunkUnload(WorldChunk chunk, CallbackInfo ci) {
		ServerChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.world, chunk);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/storage/ChunkStorage;loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/WorldChunk;"), method = "loadChunkFromStorage")
	public WorldChunk api$chunkLoad(ChunkStorage instance, World world, int i, int j, Operation<WorldChunk> original) {
		WorldChunk chunk = original.call(instance, world, i, j);
		ServerChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(this.world, chunk);
		return chunk;
	}
}
