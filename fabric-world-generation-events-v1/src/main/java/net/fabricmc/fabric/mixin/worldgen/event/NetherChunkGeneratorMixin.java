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
