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

package net.legacyfabric.fabric.impl.effect.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.api.effect.StatusEffectEvents;

public class StatusEffectRegistryImpl {
	public static final ResourceKey<Registry<StatusEffect>> KEY = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("status_effect");
	public static final Registry<StatusEffect> REGISTRY = VanillaRegistries.registerSimple(StatusEffectRegistryImpl.KEY, StatusEffect.REGISTRY);

	private static boolean locked = true;

	public static int getId(StatusEffect effect) {
		return REGISTRY.getId(effect);
	}

	public static NamespacedIdentifier getIdentifier(StatusEffect effect) {
		return REGISTRY.getIdentifier(effect);
	}

	public static ResourceKey<StatusEffect> getKey(StatusEffect effect) {
		return REGISTRY.getKey(effect);
	}

	public static StatusEffect getEffect(int id) {
		return REGISTRY.get(id);
	}

	public static StatusEffect getEffect(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static StatusEffect getEffect(ResourceKey<StatusEffect> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<StatusEffect>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, effect);
		}
	}

	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, effect);
		}
	}

	public static void init() {
		SyncedRegistries.register(StatusEffectRegistryImpl.KEY);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEffects() {
		StatusEffectEvents.REGISTER_EFFECTS.invoker().run();
	}
}
