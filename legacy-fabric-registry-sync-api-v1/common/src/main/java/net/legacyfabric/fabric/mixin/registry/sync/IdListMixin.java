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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.IdentityHashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

@Mixin(IdList.class)
public abstract class IdListMixin<V> implements IdListCompat<V> {
	@Shadow
	@Final
	private IdentityHashMap<V, Integer> idMap;

	@Shadow
	@Final
	private List<V> list;

	@Shadow
	public abstract V fromId(int index);

	@Shadow
	public abstract void set(V value, int index);

	@Shadow
	public abstract int getId(V value);

	public <K> IdentityHashMap<V, Integer> getIdMap(SimpleRegistryCompat<K, V> simpleRegistry) {
		return this.idMap;
	}

	@Override
	public List<V> getList() {
		return this.list;
	}

	@Override
	public V fromInt(int index) {
		return this.fromId(index);
	}

	@Override
	public void setValue(V value, int index) {
		this.set(value, index);
	}

	@Override
	public int getInt(V value) {
		return this.getId(value);
	}

	@Override
	public IdListCompat<V> createIdList() {
		return (IdListCompat<V>) new IdList<V>();
	}
}
