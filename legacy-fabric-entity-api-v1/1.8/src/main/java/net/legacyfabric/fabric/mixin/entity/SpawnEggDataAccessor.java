package net.legacyfabric.fabric.mixin.entity;

import net.minecraft.entity.class_868;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_868.class)
public interface SpawnEggDataAccessor {
	@Accessor
	void setField_3273(int id);
}
