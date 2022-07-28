package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.EntityType;

@Mixin(EntityType.SpawnEggData.class)
public interface SpawnEggDataAccessor {
	@Mutable
	@Accessor
	void setName(String name);
}
