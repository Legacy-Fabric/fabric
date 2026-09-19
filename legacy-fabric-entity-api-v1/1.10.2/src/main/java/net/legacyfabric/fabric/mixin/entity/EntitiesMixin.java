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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.impl.entity.EntityEventsImpl;

@Mixin(Entities.class)
public class EntitiesMixin {
	@Shadow
	@Final
	private static Map<Class<? extends Entity>, String> TYPE_TO_KEY;

	@WrapOperation(method = {"createSilently", "create(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object fixOldRegistryNames$1(Map<String, Class<? extends Entity>> instance, Object o, Operation<Class<? extends Entity>> original) {
		return EntityEventsImpl.fixOldRegistryName((String) o, (key) -> original.call(instance, key), TYPE_TO_KEY);
	}

	@WrapOperation(method = {"getId(Ljava/lang/String;)I"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false), require = 0)
	private static Object fixOldRegistryNames$2(Map<String, Class<? extends Entity>> instance, Object o, Operation<Class<? extends Entity>> original) {
		return EntityEventsImpl.fixOldRegistryName((String) o, (key) -> original.call(instance, key), TYPE_TO_KEY);
	}
}
