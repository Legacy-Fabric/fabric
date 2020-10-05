package net.fabricmc.fabric.mixin.biomes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.biome.layer.Layer;

import net.fabricmc.fabric.impl.biomes.LayerRandom;

@Mixin(Layer.class)
public abstract class MixinLayer implements LayerRandom {
	@Shadow
	protected abstract int nextInt(int i);

	@Override
	public int nextIntAccess(int bound) {
		return this.nextInt(bound);
	}
}
