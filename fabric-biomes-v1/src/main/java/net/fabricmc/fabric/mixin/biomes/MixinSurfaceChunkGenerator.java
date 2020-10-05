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

package net.fabricmc.fabric.mixin.biomes;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.feature.LakesFeature;

import net.fabricmc.fabric.api.biomes.v1.FabricBiome;

@Mixin(SurfaceChunkGenerator.class)
public abstract class MixinSurfaceChunkGenerator {
	@Unique
	private Biome fabric_localBiome;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiomeAt(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;"), method = "method_1323")
	private Biome captureLocalBiome(World world, BlockPos pos) {
		return this.fabric_localBiome = world.getBiomeAt(pos);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/LakesFeature;generate(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"), method = "method_1323")
	private boolean addModdedLakeGeneration(LakesFeature lake, World world, Random rand, BlockPos pos) {
		boolean allowed = true;

		if (this.fabric_localBiome instanceof FabricBiome) {
			allowed = ((FabricBiome) this.fabric_localBiome).isLakeAllowed(((LakesFeatureAccessor) lake).getBlock(), rand);
		}

		if (allowed) {
			return lake.generate(world, rand, pos);
		} else {
			return false;
		}
	}
}
