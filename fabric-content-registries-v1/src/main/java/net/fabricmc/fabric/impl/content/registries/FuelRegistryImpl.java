/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.impl.content.registries;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.item.Item;

import net.fabricmc.fabric.api.content.registry.v1.FuelRegistry;

@Deprecated
public class FuelRegistryImpl implements FuelRegistry {
	public static final FuelRegistryImpl INSTANCE = new FuelRegistryImpl();

	private final Map<Item, Integer> fuels = Maps.newHashMap();

	@Override
	public void register(Item fuel, int burnTime) {
		fuels.putIfAbsent(fuel, burnTime);
	}

	public Map<Item, Integer> getFuelMap() {
		return this.fuels;
	}
}
