package net.fabricmc.fabric.mixin.worldgen.event;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.EndChunkGenerator;

import net.fabricmc.fabric.api.worldgen.event.v1.BiomeDecorationEvents;

@Mixin(EndChunkGenerator.class)
public abstract class EndChunkGeneratorMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;buildSurface(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)V"), method = "method_1323")
	private void addModdedDecoration(Biome biome, World world, Random rand, BlockPos pos) {
		biome.buildSurface(world, rand, pos); // buildSurface is named incorrectly. Should be populate, decorate, or generateFeatures. Remove this comment when this naming issue is fixed.
		BiomeDecorationEvents.WORLD_DECORATION.invoker().onWorldDecorate(world, (ChunkProvider) this, rand, pos);
	}
}
