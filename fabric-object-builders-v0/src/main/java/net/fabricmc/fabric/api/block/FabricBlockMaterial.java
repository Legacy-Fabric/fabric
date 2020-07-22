package net.fabricmc.fabric.api.block;

import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public final class FabricBlockMaterial extends Material {
	private int miningLevel;

	public FabricBlockMaterial(MaterialColor color) {
		super(color);
	}

	public FabricBlockMaterial setMiningLevel(int miningLevel) {
		this.miningLevel = miningLevel;
		return this;
	}

	public int getMiningLevel() {
		return miningLevel;
	}
}
