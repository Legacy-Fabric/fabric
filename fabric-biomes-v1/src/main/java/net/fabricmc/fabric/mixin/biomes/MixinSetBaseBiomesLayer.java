package net.fabricmc.fabric.mixin.biomes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_601;
import net.minecraft.class_604;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.CustomizedWorldProperties;
import net.minecraft.world.gen.Layer;

@Mixin(class_601.class)
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

	@Overwrite
	public int[] method_1829(int startX, int startZ, int xSize, int zSize) {
		int[] previousBiomes = this.field_2272.method_1829(startX, startZ, xSize, zSize);
		int[] nextBiomes = class_604.method_1825(xSize * zSize);

		for(int zOffset = 0; zOffset < zSize; ++zOffset) {
			for(int xOffset = 0; xOffset < xSize; ++xOffset) {
				this.method_1831((long)(xOffset + startX), (long)(zOffset + startZ));
				int previousBiome = previousBiomes[xOffset + zOffset * xSize];
				int specialIndicator = ((previousBiome & 3840) >> 8);
				previousBiome &= -3841;

				if (this.field_2266 != null && this.field_2266.fixedBiome >= 0) {
					nextBiomes[xOffset + zOffset * xSize] = this.field_2266.fixedBiome;
				} else if (method_1834(previousBiome)) {
					nextBiomes[xOffset + zOffset * xSize] = previousBiome;
				} else if (previousBiome == Biome.MUSHROOM_ISLAND.field_405) {
					nextBiomes[xOffset + zOffset * xSize] = previousBiome;
				} else if (previousBiome == 1) {
					if (specialIndicator > 0) {
						if (this.method_1827(3) == 0) {
							nextBiomes[xOffset + zOffset * xSize] = Biome.MESA_PLATEAU.field_405;
						} else {
							nextBiomes[xOffset + zOffset * xSize] = Biome.MESA_PLATEAU_F.field_405;
						}
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2262[this.method_1827(this.field_2262.length)].field_405;
					}
				} else if (previousBiome == 2) {
					if (specialIndicator > 0) {
						nextBiomes[xOffset + zOffset * xSize] = Biome.JUNGLE.field_405;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2263[this.method_1827(this.field_2263.length)].field_405;
					}
				} else if (previousBiome == 3) {
					if (specialIndicator > 0) {
						nextBiomes[xOffset + zOffset * xSize] = Biome.MEGA_TAIGA.field_405;
					} else {
						nextBiomes[xOffset + zOffset * xSize] = this.field_2264[this.method_1827(this.field_2264.length)].field_405;
					}
				} else if (previousBiome == 4) {
					nextBiomes[xOffset + zOffset * xSize] = this.field_2265[this.method_1827(this.field_2265.length)].field_405;
				} else {
					nextBiomes[xOffset + zOffset * xSize] = Biome.MUSHROOM_ISLAND.field_405;
				}
			}
		}

		return nextBiomes;
	}
}
