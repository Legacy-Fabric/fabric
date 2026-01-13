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

package net.legacyfabric.fabric.mixin.enchantment;

import java.util.Arrays;
import java.util.List;

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

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.enchantment.versioned.EarlyInitializer;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayFabricRegistryWrapper;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] field_5457;
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] ALL_ENCHANTMENTS;

	@Unique
	private static FabricRegistry<Enchantment> ENCHANTMENT_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		ENCHANTMENT_REGISTRY = new SyncedArrayFabricRegistryWrapper<>(
				RegistryIds.ENCHANTMENTS,
				ALL_ENCHANTMENTS, EarlyInitializer.getVanillaIds(),
				universal -> universal,
				id -> id,
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

					ALL_ENCHANTMENTS = array;

					List<Enchantment> list = Lists.<Enchantment>newArrayList();

					for (Enchantment enchantment : ALL_ENCHANTMENTS) {
						if (enchantment != null) {
							list.add(enchantment);
						}
					}

					field_5457 = list.toArray(new Enchantment[list.size()]);
				}
		);

		RegistryHelper.addRegistry(RegistryIds.ENCHANTMENTS, ENCHANTMENT_REGISTRY);
	}
}
