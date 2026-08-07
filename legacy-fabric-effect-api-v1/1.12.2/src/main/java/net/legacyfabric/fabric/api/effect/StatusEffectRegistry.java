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

package net.legacyfabric.fabric.api.effect;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.impl.effect.versioned.StatusEffectRegistryImpl;

public final class StatusEffectRegistry {
	public static final ResourceKey<Registry<StatusEffect>> KEY = StatusEffectRegistryImpl.KEY;
	public static final Registry<StatusEffect> REGISTRY = StatusEffectRegistryImpl.REGISTRY;

	public static int getId(StatusEffect effect) {
		return StatusEffectRegistryImpl.getId(effect);
	}

	public static NamespacedIdentifier getIdentifier(StatusEffect effect) {
		return StatusEffectRegistryImpl.getIdentifier(effect);
	}

	public static ResourceKey<StatusEffect> getKey(StatusEffect effect) {
		return StatusEffectRegistryImpl.getKey(effect);
	}

	public static StatusEffect getStatusEffect(int id) {
		return StatusEffectRegistryImpl.getEffect(id);
	}

	public static StatusEffect getStatusEffect(NamespacedIdentifier identifier) {
		return StatusEffectRegistryImpl.getEffect(identifier);
	}

	public static StatusEffect getStatusEffect(ResourceKey<StatusEffect> key) {
		return StatusEffectRegistryImpl.getEffect(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return StatusEffectRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<StatusEffect>> keySet() {
		return StatusEffectRegistryImpl.keySet();
	}

	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		return StatusEffectRegistryImpl.register(identifier, effect);
	}

	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> key, T effect) {
		return StatusEffectRegistryImpl.register(key, effect);
	}
}
