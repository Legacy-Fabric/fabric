package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.IdentityHashMap;
import java.util.List;

import net.minecraft.util.IdList;

@Mixin(IdList.class)
public interface IdListAccessor {
	@Accessor
	IdentityHashMap<?, Integer> getIdMap();

	@Accessor
	List<?> getList();
}
