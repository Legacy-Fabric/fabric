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

package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.fabric.api.event.lifecycle.v1.WorldGenEvents;
import net.minecraft.class_427;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;

@Mixin(class_427.class)
public abstract class MixinOverworldChunkGenerator {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;buildSurface(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)V"), method = "method_1323")
	private void addModdedDecoration(Biome biome, World world, Random rand, BlockPos pos) {
		biome.buildSurface(world, rand, pos); // buildSurface is named incorrectly. Should be populate, decorate, or generateFeatures. Remove this comment when this naming issue is fixed.
		WorldGenEvents.WORLD_DECORATION.invoker().onWorldDecorate(world, (ChunkProvider) this, rand, pos);
	}
}
