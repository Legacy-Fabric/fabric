/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.legacyfabric.fabric.api.registry.v1.EntityTypeIds;
import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.util.OldRemappedRegistry;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	@Shadow
	public static String getEntityName(Entity entity) {
		return null;
	}

	@ModifyArg(method = {"createInstanceFromName", "createInstanceFromNbt", "getIdByName"},
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
	private static Object remap$createInstanceFromName(Object key) {
		RegistryRemapper<Class<? extends Entity>> remapper = RegistryHelperImpl.getRegistryRemapper(RegistryIds.ENTITY_TYPES);
		OldRemappedRegistry<String, Class<? extends Entity>> registry = (OldRemappedRegistry<String, Class<? extends Entity>>) remapper.getRegistry();
		return registry.getNewKey(key.toString());
	}

	@Inject(method = "getEntityNames", at = @At("RETURN"))
	private static void remap$getEntityNames(CallbackInfoReturnable<List<String>> cir) {
		List<String> list = cir.getReturnValue();
		list.remove("LightningBolt");
		list.add(EntityTypeIds.LIGHTNING_BOLT.toString());
	}

	@Inject(method = "equals", at = @At("RETURN"), cancellable = true)
	private static void remap$equals(Entity entity, String string, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			String string2 = getEntityName(entity);

			if (string2 == null) {
				if (entity instanceof PlayerEntity) {
					string2 = EntityTypeIds.PLAYER.toString();
				} else {
					if (!(entity instanceof LightningBoltEntity)) {
						cir.setReturnValue(false);
					}

					string2 = EntityTypeIds.LIGHTNING_BOLT.toString();
				}
			}

			RegistryRemapper<Class<? extends Entity>> remapper = RegistryHelperImpl.getRegistryRemapper(RegistryIds.ENTITY_TYPES);
			OldRemappedRegistry<String, Class<? extends Entity>> registry = (OldRemappedRegistry<String, Class<? extends Entity>>) remapper.getRegistry();

			cir.setReturnValue(registry.getNewKey(string).equals(string2));
		}
	}

	@ModifyArg(method = "isEntityRegistered", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z", remap = false))
	private static Object remap$isEntityRegistered(Object key) {
		RegistryRemapper<Class<? extends Entity>> remapper = RegistryHelperImpl.getRegistryRemapper(RegistryIds.ENTITY_TYPES);
		OldRemappedRegistry<String, Class<? extends Entity>> registry = (OldRemappedRegistry<String, Class<? extends Entity>>) remapper.getRegistry();
		return registry.getNewKey(key.toString());
	}

	@Inject(method = "isEntityRegistered", cancellable = true, at = @At("RETURN"))
	private static void remap$isEntityRegistered(String name, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			cir.setReturnValue(name.equals(EntityTypeIds.PLAYER.toString()));
		}
	}
}
