package net.legacyfabric.fabric.mixin.entity;

import net.minecraft.entity.EntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityType.SpawnEggData.class)
public interface SpawnEggDataAccessor {
	@Accessor
	void setId(int id);
}
