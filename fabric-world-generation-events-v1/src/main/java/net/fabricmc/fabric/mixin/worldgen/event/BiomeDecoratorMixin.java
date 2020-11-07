package net.fabricmc.fabric.mixin.worldgen.event;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.CustomizedWorldProperties;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.fabric.api.worldgen.event.v1.BiomeDecorationEvents;
import net.fabricmc.fabric.impl.worldgen.event.OreCancellable;
import net.fabricmc.fabric.impl.worldgen.event.OreRegistryImpl;

@Mixin(BiomeDecorator.class)
public abstract class BiomeDecoratorMixin {
	@Shadow
	protected World world;

	@Shadow
	protected CustomizedWorldProperties worldProperties;

	@Shadow
	protected abstract void generateOre(int count, Feature feature, int minHeight, int maxHieght);

	@Unique
	private Biome capturedBiome;

	@Inject(method = "generate", at = @At("HEAD"))
	public void captureBiome(Biome biome, CallbackInfo ci) {
		this.capturedBiome = biome;
	}

	@Inject(method = "generateOres", at = @At("TAIL"))
	public void callOreGenEvent(CallbackInfo ci) {
		OreRegistryImpl registry = new OreRegistryImpl();
		BiomeDecorationEvents.ORE_GENERATION.invoker().accept(registry, this.world, this.capturedBiome, this.worldProperties);
		registry.forEach(this::generateOre);
	}

	@Inject(method = "generateOre", at = @At("HEAD"), cancellable = true)
	public void cancelOreGen(int count, Feature feature, int minHeight, int maxHeight, CallbackInfo ci) {
		this.cancel(count, feature, minHeight, maxHeight, ci);
	}

	@Inject(method = "method_565", at = @At("HEAD"), cancellable = true)
	public void cancelLapisGen(int count, Feature feature, int minHeight, int maxHeight, CallbackInfo ci) {
		this.cancel(count, feature, minHeight, maxHeight, ci);
	}

	@Unique
	private void cancel(int count, Feature feature, int minHeight, int maxHeight, CallbackInfo ci) {
		OreCancellable c = new OreCancellable();
		BiomeDecorationEvents.ORE_CANCELLATION.invoker().accept(
				c,
				count,
				(OreFeature) feature,
				minHeight,
				maxHeight,
				this.capturedBiome,
				this.world,
				this.worldProperties
		);

		if (c.isCancelled()) {
			ci.cancel();
		}
	}
}
