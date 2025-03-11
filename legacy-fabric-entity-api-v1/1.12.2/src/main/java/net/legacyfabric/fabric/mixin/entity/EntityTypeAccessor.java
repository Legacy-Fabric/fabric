package net.legacyfabric.fabric.mixin.entity;

import net.minecraft.entity.EntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {
	@Accessor
	static List<String> getNAMES() {
		return null;
	}

	@Accessor
	static void setNAMES(List<String> names) {

	}
}
