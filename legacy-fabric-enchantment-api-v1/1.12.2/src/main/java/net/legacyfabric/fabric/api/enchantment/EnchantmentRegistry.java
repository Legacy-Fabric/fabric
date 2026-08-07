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

package net.legacyfabric.fabric.api.enchantment;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

import net.minecraft.enchantment.Enchantment;

import net.legacyfabric.fabric.impl.enchantment.versioned.EnchantmentRegistryImpl;

public final class EnchantmentRegistry {
	public static final ResourceKey<Registry<Enchantment>> KEY = EnchantmentRegistryImpl.KEY;
	public static final Registry<Enchantment> REGISTRY = EnchantmentRegistryImpl.REGISTRY;

	public static int getId(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getId(enchantment);
	}

	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getIdentifier(enchantment);
	}

	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getKey(enchantment);
	}

	public static Enchantment getEnchantment(int id) {
		return EnchantmentRegistryImpl.getEnchantment(id);
	}

	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return EnchantmentRegistryImpl.getEnchantment(identifier);
	}

	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return EnchantmentRegistryImpl.getEnchantment(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return EnchantmentRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<Enchantment>> keySet() {
		return EnchantmentRegistryImpl.keySet();
	}

	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T type) {
		return EnchantmentRegistryImpl.register(identifier, type);
	}

	public static <T extends Enchantment> T register(ResourceKey<Enchantment> key, T type) {
		return EnchantmentRegistryImpl.register(key, type);
	}
}
