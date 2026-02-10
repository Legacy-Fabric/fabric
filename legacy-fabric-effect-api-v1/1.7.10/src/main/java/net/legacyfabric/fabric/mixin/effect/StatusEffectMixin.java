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

package net.legacyfabric.fabric.mixin.effect;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.impl.effect.versioned.EarlyInitializer;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayFabricRegistryWrapper;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@Mutable
	@Shadow
	@Final
	public static StatusEffect[] BY_ID;

	@Unique
	private static FabricRegistry<StatusEffect> STATUS_EFFECT_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		STATUS_EFFECT_REGISTRY = new SyncedArrayFabricRegistryWrapper<>(
				RegistryIds.STATUS_EFFECTS,
				BY_ID, EarlyInitializer.getVanillaIds(),
				universal -> universal,
				id -> id,
				ids -> {
					StatusEffect[] array = new StatusEffect[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (StatusEffect effect : ids) {
						int id = ids.fabric$getId(effect);

						if (id >= array.length - 1) {
							StatusEffect[] newArray = new StatusEffect[id + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = effect;
					}

					BY_ID = array;
				},
				1
		);

		RegistryHelper.addRegistry(RegistryIds.STATUS_EFFECTS, STATUS_EFFECT_REGISTRY);
	}
}
