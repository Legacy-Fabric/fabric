package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor {
	@Accessor
	IdList<?> getIds();

	@Accessor
	Map<?, Identifier> getObjects();

	@Mutable
	@Accessor
	void setIds(IdList<?> ids);
}
