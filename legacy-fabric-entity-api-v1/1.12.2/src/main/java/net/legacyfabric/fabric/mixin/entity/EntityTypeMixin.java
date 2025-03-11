package net.legacyfabric.fabric.mixin.entity;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityType.class)
public class EntityTypeMixin {
	@Shadow
	@Final
	public static SimpleRegistry<Identifier, Class<? extends Entity>> REGISTRY;

	@Inject(method = "load", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		RegistryHelper.addRegistry(RegistryIds.ENTITY_TYPES, REGISTRY);
	}
}
