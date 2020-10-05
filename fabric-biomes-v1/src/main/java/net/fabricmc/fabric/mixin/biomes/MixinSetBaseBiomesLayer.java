package net.fabricmc.fabric.mixin.biomes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_604;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.Layer;
import net.minecraft.world.biome.layer.SetBaseBiomesLayer;
import net.minecraft.world.gen.CustomizedWorldProperties;

import net.fabricmc.fabric.api.biomes.v1.Climate;
import net.fabricmc.fabric.impl.biomes.InternalBiomeUtils;
import net.fabricmc.fabric.impl.biomes.LayerRandom;

@Mixin(SetBaseBiomesLayer.class)
public abstract class MixinSetBaseBiomesLayer extends Layer {
	public MixinSetBaseBiomesLayer(long l) {
		super(l);
	}

	@Shadow
	private Biome[] field_2262;
	@Shadow
	private Biome[] field_2263;
	@Shadow
	private Biome[] field_2264;
	@Shadow
	private Biome[] field_2265;

	@Shadow
	@Final
	private CustomizedWorldProperties field_2266;

	/**
	 * @author Valoeghese
	 * @author too lazy to inject? no idea
	 */
	@Overwrite
	public int[] method_1829(int startX, int startZ, int xSize, int zSize) {
		int[] previousBiomes = this.field_2272.method_1829(startX, startZ, xSize, zSize);
		int[] nextBiomes = class_604.method_1825(xSize * zSize);
		LayerRandom random = (LayerRandom) this;

		for (int zOffset = 0; zOffset < zSize; ++zOffset) {
			for (int xOffset = 0; xOffset < xSize; ++xOffset) {
				this.method_1831(xOffset + startX, zOffset + startZ);
				int previousBiome = previousBiomes[xOffset + zOffset * xSize];
				int specialIndicator = ((previousBiome & 3840) >> 8);
				previousBiome &= -3841;

				if (this.field_2266 != null && this.field_2266.fixedBiome >= 0) {
					nextBiomes[xOffset + zOffset * xSize] = this.field_2266.fixedBiome;
				} else if (previousBiome == Biome.OCEAN.id) {
					nextBiomes[xOffset + zOffset * xSize] = Climate.OCEAN.pickBiome(random).id;
				} else if (previousBiome == Biome.DEEP_OCEAN.id) {
					nextBiomes[xOffset + zOffset * xSize] = InternalBiomeUtils.getDeepOcean(Climate.OCEAN.pickBiome(random)).id;
				} else if (previousBiome == Biome.FROZEN_OCEAN.id) {
					nextBiomes[xOffset + zOffset * xSize] = Climate.FROZEN_OCEAN.pickBiome(random).id;
				} else if (previousBiome == Biome.MUSHROOM_ISLAND.id) {
					nextBiomes[xOffset + zOffset * xSize] = Climate.ISLAND.pickBiome(random).id;
				} else if (previousBiome == 1) {
					if (specialIndicator > 0) {
						if (this.nextInt(3) == 0) {
							nextBiomes[xOffset + zOffset * xSize] = Biome.MESA_PLATEAU.id;
						} else {
							nextBiomes[xOffset + zOffset * xSize] = Biome.MESA_PLATEAU_F.id;
						}
					} else if (Climate.DRY.isModded()) {
						nextBiomes[xOffset + zOffset * xSize] = Climate.DRY.pickBiome(random).id;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2262[this.nextInt(this.field_2262.length)].id;
					}
				} else if (previousBiome == 2) {
					if (specialIndicator > 0) {
						nextBiomes[xOffset + zOffset * xSize] = Biome.JUNGLE.id;
					} else if (Climate.TEMPERATE.isModded()) {
						nextBiomes[xOffset + zOffset * xSize] = Climate.TEMPERATE.pickBiome(random).id;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2263[this.nextInt(this.field_2263.length)].id;
					}
				} else if (previousBiome == 3) {
					if (specialIndicator > 0) {
						nextBiomes[xOffset + zOffset * xSize] = Biome.MEGA_TAIGA.id;
					} else if (Climate.COOL.isModded()) {
						nextBiomes[xOffset + zOffset * xSize] = Climate.COOL.pickBiome(random).id;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2264[this.nextInt(this.field_2264.length)].id;
					}
				} else if (previousBiome == 4) {
					if (Climate.ICY.isModded()) {
						nextBiomes[xOffset + zOffset * xSize] = Climate.ICY.pickBiome(random).id;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2265[this.nextInt(this.field_2265.length)].id;
					}
				} else {
					Climate climate = InternalBiomeUtils.getClimate(previousBiome);

					if (climate != null) {
						nextBiomes[xOffset + zOffset * xSize] = climate.pickBiome(random).id;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = Biome.MUSHROOM_ISLAND.id;
					}
				}
			}
		}

		return nextBiomes;
	}
}
