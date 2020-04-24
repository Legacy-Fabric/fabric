package net.fabricmc.fabric.mixin.content.registries;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.registry.MutableRegistry;

@SuppressWarnings("rawtypes")
@Mixin(MutableRegistry.class)
public interface MutableRegistryAccessor {
	@Accessor("map")
	Map getMap();
}
