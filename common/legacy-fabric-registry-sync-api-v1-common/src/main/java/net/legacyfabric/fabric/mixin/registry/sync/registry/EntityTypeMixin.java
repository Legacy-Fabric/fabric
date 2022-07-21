package net.legacyfabric.fabric.mixin.registry.sync.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.EntityTypeRegistryRemapper;

@Mixin(EntityType.class)
public class EntityTypeMixin {
	@Inject(method = "load", at = @At("RETURN"))
	private static void initRegistry(CallbackInfo ci) {
		RegistryHelperImpl.registerRegistryRemapper(EntityTypeRegistryRemapper::new);
	}
}
