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

package net.legacyfabric.fabric.mixin.event.lifecycle.client;

import net.minecraft.client.world.ClientChunkCache;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;

@Environment(EnvType.CLIENT)
@Mixin(ClientChunkCache.class)
public abstract class ClientChunkCacheMixin {
	@Final
	@Shadow
	private World world;

	@Shadow
	@Nullable
	public abstract Chunk getLoadedChunk(int x, int z);

	@Inject(at = @At("RETURN"), method = "method_29930")
	public void chunkUnload(int i, int j, CallbackInfo ci) {
		ClientChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload((ClientWorld) this.world, this.getLoadedChunk(i, j));
	}

	@Inject(at = @At("RETURN"), method = "method_29931", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void chunkLoad(int i, int j, CallbackInfoReturnable<Chunk> cir, Chunk chunk) {
		ClientChunkEvents.CHUNK_LOAD.invoker().onChunkLoad((ClientWorld) this.world, chunk);
	}
}
