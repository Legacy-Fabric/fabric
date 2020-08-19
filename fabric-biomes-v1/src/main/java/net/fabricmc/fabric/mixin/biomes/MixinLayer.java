package net.fabricmc.fabric.mixin.biomes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.fabric.impl.biomes.LayerRandom;
import net.minecraft.world.gen.Layer;

@Mixin(Layer.class)
public abstract class MixinLayer implements LayerRandom {
	@Override
	public int nextInt(int bound) {
		return this.method_1827(bound);
	}

	@Shadow
	protected abstract int method_1827(int bound);
}
