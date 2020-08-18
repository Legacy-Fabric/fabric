package net.fabricmc.fabric.mixin.biomes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.LakesFeature;

@Mixin(LakesFeature.class)
public interface AccessorLakesFeature {
	@Accessor("field_1932")
	Block getBlock();
}
