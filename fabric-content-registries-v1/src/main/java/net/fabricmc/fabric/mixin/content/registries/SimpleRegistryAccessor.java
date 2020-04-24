package net.fabricmc.fabric.mixin.content.registries;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.IdList;
import net.minecraft.util.registry.SimpleRegistry;

@SuppressWarnings("rawtypes")
@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor {
	@Accessor("ids")
	void setIds(IdList ids);

	@Accessor("objects")
	Map getObjects();
}
