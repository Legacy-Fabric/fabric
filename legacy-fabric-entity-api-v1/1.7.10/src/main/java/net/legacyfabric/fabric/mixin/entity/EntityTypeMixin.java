package net.legacyfabric.fabric.mixin.entity;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.entity.MapEntityRegistryWrapper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityType.class)
public class EntityTypeMixin {
	@Shadow
	private static Map<String, Class<? extends Entity>> NAME_CLASS_MAP;
	@Shadow
	private static Map<Class<? extends Entity>, String> CLASS_NAME_MAP;
	@Shadow
	private static Map<Integer, Class<? extends Entity>> ID_CLASS_MAP;
	@Shadow
	private static Map<Class<? extends Entity>, Integer> CLASS_ID_MAP;
	@Shadow
	private static Map<String, Integer> field_3272;

	@Unique
	private static Registry<Class<? extends Entity>> ENTITY_TYPE_REGISTRY;

	@Inject(method = "load", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		ENTITY_TYPE_REGISTRY = new MapEntityRegistryWrapper<>(
				NAME_CLASS_MAP,
				CLASS_NAME_MAP,
				ID_CLASS_MAP,
				CLASS_ID_MAP,
				field_3272
		);

		RegistryHelper.addRegistry(RegistryIds.ENTITY_TYPES, ENTITY_TYPE_REGISTRY);
	}

	@ModifyArg(method = {"createInstanceFromName", "createInstanceFromNbt"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object fixOldRegistryNames(Object o) {
		String key = (String) o;

		if (key.contains(":")) {
			Identifier identifier = new Identifier(key);
			Class<? extends Entity> clazz = RegistryHelper.getValue(RegistryIds.ENTITY_TYPES, identifier);

			if (clazz != null) {
				key = CLASS_NAME_MAP.get(clazz);
			}
		}

		return key;
	}
}
