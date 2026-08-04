/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

import net.legacyfabric.fabric.api.entity.EntityRegistry;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;
import net.legacyfabric.fabric.impl.entity.versionned.EntityRegistryImpl;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.IntegerMapMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Mixin(Entities.class)
public class EntitiesMixin {
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

	@Shadow
	@Final
	public static Map<Integer, Entities.SpawnEggData> SPAWN_EGG_DATA;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void lf$unlockRegistry(CallbackInfo ci) {
		EntityRegistryImpl.unlock();
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		EntityRegistryImpl.registerEntityTypes();

		SyncedRegistries.registerMapper(VanillaRegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.parse("entity_type/id_to_type"), IntegerMapMapper.of(ID_TO_TYPE));
		SyncedRegistries.registerFixer(VanillaRegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.parse("entity_type/type_to_id"), () -> {
			TYPE_TO_ID.clear();

			for (Map.Entry<Integer, Class<? extends Entity>> entry : ID_TO_TYPE.entrySet()) {
				TYPE_TO_ID.put(entry.getValue(), entry.getKey());
			}
		});
		SyncedRegistries.registerFixer(VanillaRegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.parse("entity_type/key_to_id"), () -> {
			KEY_TO_ID.clear();

			for (Map.Entry<Integer, Class<? extends Entity>> entry : ID_TO_TYPE.entrySet()) {
				KEY_TO_ID.put(TYPE_TO_KEY.get(entry.getValue()), entry.getKey());
			}
		});
		SyncedRegistries.registerMapper(VanillaRegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.parse("entity_type/spawn_egg_data"), IntegerMapMapper.of(SPAWN_EGG_DATA));
	}

	@ModifyArg(method = {"createSilently", "create(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object fixOldRegistryNames(Object o) {
		String key = (String) o;

		if (key.contains(":")) {
			NamespacedIdentifier identifier = NamespacedIdentifiers.parse(key);
			Class<? extends Entity> clazz = EntityRegistry.getEntityType(identifier);

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
			NamespacedIdentifier identifier = NamespacedIdentifiers.parse(key);
			Class<? extends Entity> clazz = EntityRegistry.getEntityType(identifier);

			if (clazz != null) {
				key = TYPE_TO_KEY.get(clazz);
			}
		}

		return key;
	}
}
