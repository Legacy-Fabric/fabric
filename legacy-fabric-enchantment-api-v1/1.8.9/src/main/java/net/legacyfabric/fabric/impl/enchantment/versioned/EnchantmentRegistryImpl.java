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

package net.legacyfabric.fabric.impl.enchantment.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

import net.minecraft.enchantment.Enchantment;

import net.legacyfabric.fabric.api.enchantment.EnchantmentEvents;

public class EnchantmentRegistryImpl {
	public static final ResourceKey<Registry<Enchantment>> KEY = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("enchantment");
	public static final Registry<Enchantment> REGISTRY = Registries.registerSimple(EnchantmentRegistryImpl.KEY, () -> {
		Enchantment[] temp = Enchantment.BY_ID;
	});

	private static boolean locked = true;

	public static int getId(Enchantment enchantment) {
		return REGISTRY.getId(enchantment);
	}

	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return REGISTRY.getIdentifier(enchantment);
	}

	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return REGISTRY.getKey(enchantment);
	}

	public static Enchantment getEnchantment(int id) {
		return REGISTRY.get(id);
	}

	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Enchantment>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static <T extends Enchantment> T register(ResourceKey<Enchantment> identifier, T type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static void init() {
		SyncedRegistries.register(EnchantmentRegistryImpl.KEY);
		SyncedRegistries.registerFixer(EnchantmentRegistryImpl.KEY, NamespacedIdentifiers.from("enchantment/id"), new EnchantmentIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEnchantments() {
		VanillaEnchantments.init();
		EnchantmentEvents.REGISTER_ENCHANTMENTS.invoker().run();
	}
}
