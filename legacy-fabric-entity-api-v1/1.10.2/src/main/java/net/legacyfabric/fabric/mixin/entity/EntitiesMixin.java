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

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

@Mixin(Entities.class)
public class EntitiesMixin {
	@Shadow
	@Final
	private static Map<Class<? extends Entity>, String> TYPE_TO_KEY;

	@ModifyArg(method = {"createSilently", "create(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;", "getId(Ljava/lang/String;)I"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object fixOldRegistryNames(Object o) {
		String key = (String) o;

		if (key.contains(".")) {
			NamespacedIdentifier identifier = NamespacedIdentifiers.parse(key.replace(".", ":"));
			Class<? extends Entity> clazz = EntityTypeRegistry.getEntityType(identifier);

			if (clazz != null) {
				key = TYPE_TO_KEY.get(clazz);
			}
		}

		return key;
	}
}
