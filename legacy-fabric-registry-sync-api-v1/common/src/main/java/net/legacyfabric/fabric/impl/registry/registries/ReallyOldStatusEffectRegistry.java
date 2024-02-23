/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.impl.registry.registries;

import com.google.common.collect.BiMap;

import net.minecraft.entity.effect.StatusEffect;

import net.legacyfabric.fabric.api.registry.v1.StatusEffectIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.util.ArrayBasedRegistry;

public abstract class ReallyOldStatusEffectRegistry extends ArrayBasedRegistry<StatusEffect> {
	public ReallyOldStatusEffectRegistry(StatusEffect[] valueArray) {
		super(valueArray);
	}

	@Override
	public BiMap<Integer, Identifier> generateIdToKeyMap() {
		BiMap<Integer, Identifier> map = super.generateIdToKeyMap();

		map.put(1, StatusEffectIds.SPEED);
		map.put(2, StatusEffectIds.SLOWNESS);
		map.put(3, StatusEffectIds.HASTE);
		map.put(4, StatusEffectIds.MINING_FATIGUE);
		map.put(5, StatusEffectIds.STRENGTH);
		map.put(6, StatusEffectIds.INSTANT_HEALTH);
		map.put(7, StatusEffectIds.INSTANT_DAMAGE);
		map.put(8, StatusEffectIds.JUMP_BOOST);
		map.put(9, StatusEffectIds.NAUSEA);
		map.put(10, StatusEffectIds.REGENERATION);
		map.put(11, StatusEffectIds.RESISTANCE);
		map.put(12, StatusEffectIds.FIRE_RESISTANCE);
		map.put(13, StatusEffectIds.WATER_BREATHING);
		map.put(14, StatusEffectIds.INVISIBILITY);
		map.put(15, StatusEffectIds.BLINDNESS);
		map.put(16, StatusEffectIds.NIGHT_VISION);
		map.put(17, StatusEffectIds.HUNGER);
		map.put(18, StatusEffectIds.WEAKNESS);
		map.put(19, StatusEffectIds.POISON);
		map.put(20, StatusEffectIds.WITHER);
		map.put(21, StatusEffectIds.HEALTH_BOOST);
		map.put(22, StatusEffectIds.ABSORPTION);
		map.put(23, StatusEffectIds.SATURATION);

		return map;
	}
}
