package net.fabricmc.fabric.test.biome;

import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeFeatureRegistry;
import net.fabricmc.fabric.mixin.biome.BiomeAccessor;

public class BiomeTest implements ModInitializer {
	public static final OreFeature TEST_ORE = BiomeFeatureRegistry.INSTANCE.registerOreFeature(new OreFeature(Blocks.NETHER_QUARTZ_ORE.getDefaultState(), 15, BlockPredicate.make(Blocks.DIRT)), 4, 11, 70, BiomeAccessor.getAllBiomes());

	@Override
	public void onInitialize() {

	}
}
