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

package net.legacyfabric.fabric.mixin.item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.ItemRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.util.registry.IdRegistry;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.registry.OrnithableRegistry;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	@Final
	public static IdRegistry REGISTRY;

	@Inject(method = "init", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		RegistryHelper.addRegistry(RegistryIds.ITEMS, (FabricRegistry<?>) REGISTRY);
		((OrnithableRegistry<Object, Item>) REGISTRY).setRegisterFunction((id, key, value) -> {
			ItemRegistry.register(id, NamespacedIdentifiers.parse(key.toString()), value);
			return null;
		});
	}
}
