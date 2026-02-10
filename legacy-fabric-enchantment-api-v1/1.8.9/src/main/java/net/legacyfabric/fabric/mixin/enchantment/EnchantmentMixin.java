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

package net.legacyfabric.fabric.mixin.enchantment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.Identifier;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayMapFabricRegistryWrapper;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Mutable
	@Shadow
	@Final
	private static Map<Identifier, Enchantment> BY_KEY;
	@Mutable
	@Shadow
	@Final
	private static Enchantment[] BY_ID;
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] ALL;
	@Unique
	private static FabricRegistry<Enchantment> ENCHANTMENT_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		BiMap<Identifier, Enchantment> map = HashBiMap.create(BY_KEY);
		BY_KEY = map;

		ENCHANTMENT_REGISTRY = new SyncedArrayMapFabricRegistryWrapper<>(
				RegistryIds.ENCHANTMENTS,
				BY_ID, map, false,
				universal -> new Identifier(universal.toString()),
				net.legacyfabric.fabric.api.util.Identifier::new,
				ids -> {
					Enchantment[] array = new Enchantment[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (Enchantment enchantment : ids) {
						int id = ids.fabric$getId(enchantment);

						if (id >= array.length - 1) {
							Enchantment[] newArray = new Enchantment[id + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = enchantment;
					}

					BY_ID = array;

					List<Enchantment> list = Lists.<Enchantment>newArrayList();

					for (Enchantment enchantment : BY_ID) {
						if (enchantment != null) {
							list.add(enchantment);
						}
					}

					ALL = list.toArray(new Enchantment[list.size()]);
				}
		);

		RegistryHelper.addRegistry(RegistryIds.ENCHANTMENTS, ENCHANTMENT_REGISTRY);
	}
}
