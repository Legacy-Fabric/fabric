/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.mixin.entity;

import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.entity.MapEntityRegistryWrapper;

@Mixin(Entities.class)
public class EntityTypeMixin {
	@Shadow
	@Final
	private static Map<String, Class<? extends Entity>> KEY_TO_TYPE;
	@Shadow
	@Final
	private static Map<Class<? extends Entity>, String> TYPE_TO_KEY;
	@Shadow
	@Final
	private static Map<Integer, Class<? extends Entity>> ID_TO_TYPE;
	@Shadow
	@Final
	private static Map<Class<? extends Entity>, Integer> TYPE_TO_ID;
	@Shadow
	@Final
	private static Map<String, Integer> KEY_TO_ID;

	@Unique
	private static FabricRegistry<Class<? extends Entity>> ENTITY_TYPE_REGISTRY;

	@Inject(method = "init", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		ENTITY_TYPE_REGISTRY = new MapEntityRegistryWrapper<>(
				KEY_TO_TYPE,
				TYPE_TO_KEY,
				ID_TO_TYPE,
				TYPE_TO_ID,
				KEY_TO_ID
		);

		RegistryHelper.addRegistry(RegistryIds.ENTITY_TYPES, ENTITY_TYPE_REGISTRY);
	}

	@ModifyArg(method = {"createSilently", "create(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object fixOldRegistryNames(Object o) {
		String key = (String) o;

		if (key.contains(":")) {
			Identifier identifier = new Identifier(key);
			Class<? extends Entity> clazz = RegistryHelper.getValue(RegistryIds.ENTITY_TYPES, identifier);

			if (clazz != null) {
				key = TYPE_TO_KEY.get(clazz);
			}
		}

		return key;
	}

	@Environment(EnvType.CLIENT)
	@ModifyArg(method = {"getId(Ljava/lang/String;)I"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object client$fixOldRegistryNames(Object o) {
		String key = (String) o;

		if (key.contains(":")) {
			Identifier identifier = new Identifier(key);
			Class<? extends Entity> clazz = RegistryHelper.getValue(RegistryIds.ENTITY_TYPES, identifier);

			if (clazz != null) {
				key = TYPE_TO_KEY.get(clazz);
			}
		}

		return key;
	}
}
