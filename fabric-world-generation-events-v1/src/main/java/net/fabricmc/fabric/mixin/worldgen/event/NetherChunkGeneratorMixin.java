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

package net.fabricmc.fabric.mixin.worldgen.event;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.structure.NetherFortressFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.NetherChunkGenerator;

import net.fabricmc.fabric.api.worldgen.event.v1.BiomeDecorationEvents;

@Mixin(NetherChunkGenerator.class)
public class NetherChunkGeneratorMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/NetherFortressFeature;method_1690(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/ChunkPos;)Z"), method = "method_1323")
	private boolean addModdedDecoration(NetherFortressFeature feature, World world, Random rand, ChunkPos cpos) {
		boolean result = feature.method_1690(world, rand, cpos);
		BlockPos pos = new BlockPos(cpos.x * 16, 0, cpos.z * 16);
		BiomeDecorationEvents.WORLD_DECORATION.invoker().onWorldDecorate(world, (ChunkProvider) this, rand, pos);
		return result;
	}
}
