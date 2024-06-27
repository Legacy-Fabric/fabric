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

package net.legacyfabric.fabric.mixin.event.lifecycle.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ServerChunkProvider;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerChunkEvents;

@Mixin(ServerChunkProvider.class)
public class ServerChunkProviderMixin {
	@Shadow
	private ServerWorld world;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkStorage;writeChunk(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/Chunk;)V"), method = "saveChunk")
	public void api$chunkUnload(Chunk chunk, CallbackInfo ci) {
		ServerChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.world, chunk);
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/chunk/ChunkStorage;loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/Chunk;"), method = "loadChunk", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void api$chunkLoad(int x, int z, CallbackInfoReturnable<Chunk> cir, Chunk chunk) {
		ServerChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(this.world, chunk);
	}
}
