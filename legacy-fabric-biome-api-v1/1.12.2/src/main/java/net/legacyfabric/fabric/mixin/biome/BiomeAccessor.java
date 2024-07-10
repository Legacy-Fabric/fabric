package net.legacyfabric.fabric.mixin.biome;

import net.minecraft.util.collection.IdList;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface BiomeAccessor {
	@Accessor
	String getParent();

	@Accessor
	static void setBiomeList(IdList<Biome> idList) {

	}

	@Accessor
	void setName(String name);
}
