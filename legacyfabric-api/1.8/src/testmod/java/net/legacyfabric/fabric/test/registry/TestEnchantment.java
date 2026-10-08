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

package net.legacyfabric.fabric.test.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;

public class TestEnchantment extends Enchantment {
	protected TestEnchantment(NamespacedIdentifier identifier) {
		super(REGISTRY_AUTO_ASSIGN_ID, new Identifier(identifier.toString()), 2, EnchantmentCategory.ARMOR_FEET);
	}

	@Override
	public void applyDamageWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT.id, 50, 10));
	}

	@Override
	public void applyProtectionWildcard(LivingEntity bearer, Entity entity, int power) {
		bearer.addStatusEffect(new StatusEffectInstance(RegistryTest.EFFECT.id, 50, 10));
	}
}
