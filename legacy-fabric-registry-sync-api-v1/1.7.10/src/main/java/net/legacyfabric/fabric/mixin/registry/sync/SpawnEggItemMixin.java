/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;

import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.util.OldRemappedRegistry;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
	@Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;getEntityName(I)Ljava/lang/String;"))
	private String saveAsVanillaId(int id) {
		RegistryRemapper<Class<? extends Entity>> remapper = RegistryHelperImpl.getRegistryRemapper(RegistryIds.ENTITY_TYPES);
		if (remapper == null) return EntityType.getEntityName(id);
		OldRemappedRegistry<String, Class<? extends Entity>> registry = (OldRemappedRegistry<String, Class<? extends Entity>>) remapper.getRegistry();

		return registry.getOldKey(EntityType.getEntityName(id));
	}
}
