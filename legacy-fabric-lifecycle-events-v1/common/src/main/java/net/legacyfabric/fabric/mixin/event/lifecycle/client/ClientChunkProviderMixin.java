/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.event.lifecycle.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ClientChunkProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;

@Environment(EnvType.CLIENT)
@Mixin(ClientChunkProvider.class)
public abstract class ClientChunkProviderMixin {
	@Shadow
	private World world;

	@Inject(at = @At("RETURN"), method = "getOrGenerateChunk", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void chunkLoad(int i, int j, CallbackInfoReturnable<Chunk> cir, Chunk chunk) {
		ClientChunkEvents.CHUNK_LOAD.invoker().onChunkLoad((ClientWorld) this.world, chunk);
	}
}
