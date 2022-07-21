package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {
	@Mutable
	@Accessor
	static Map<String, Class<? extends Entity>> getNAME_CLASS_MAP() {
		return null;
	}

	@Mutable
	@Accessor
	static void setNAME_CLASS_MAP(Map<String, Class<? extends Entity>> map) {
	}

	@Mutable
	@Accessor
	static void setCLASS_NAME_MAP(Map<Class<? extends Entity>, String> map) {
	}

	@Mutable
	@Accessor
	static Map<Integer, Class<? extends Entity>> getID_CLASS_MAP() {
		return null;
	}

	@Mutable
	@Accessor
	static void setID_CLASS_MAP(Map<Integer, Class<? extends Entity>> map) {
	}

	@Mutable
	@Accessor
	static void setCLASS_ID_MAP(Map<Class<? extends Entity>, Integer> map) {
	}

	@Mutable
	@Accessor
	static Map<String, Integer> getNAME_ID_MAP() {
		return null;
	}

	@Mutable
	@Accessor
	static void setNAME_ID_MAP(Map<String, Integer> map) {
	}
}
