/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.impl.registry.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public abstract class ArrayBasedRegistry<V> implements SimpleRegistryCompat<Identifier, V> {
	private V[] valueArray;
	private final Map<Identifier, V> defaultMap = HashBiMap.create();
	private final Map<V, Identifier> invertedMap = ((BiMap<Identifier, V>) defaultMap).inverse();
	private IdListCompat<V> IDLIST = (IdListCompat<V>) new IdList<V>();

	public ArrayBasedRegistry(V[] valueArray) {
		this.valueArray = (V[]) Array.newInstance(valueArray.getClass().getComponentType(), 1);

		Map<Integer, Identifier> defaultIds = this.getDefaultIds();

		for (int i = 0; i < valueArray.length; i++) {
			this.register(i, defaultIds.getOrDefault(i, new Identifier("modded", String.valueOf(i))), valueArray[i], false);
		}

		this.syncArrayWithIdList();
	}

	public abstract Map<Integer, Identifier> getDefaultIds();

	@Override
	public IdListCompat<V> getIds() {
		return IDLIST;
	}

	@Override
	public Map<V, Identifier> getObjects() {
		return this.invertedMap;
	}

	@Override
	public void setIds(IdListCompat<V> idList) {
		this.IDLIST = idList;
	}

	@Override
	public IdListCompat<V> createIdList() {
		return (IdListCompat<V>) new IdList<V>();
	}

	@Override
	public int getRawID(V object) {
		return IDLIST.getInt(object);
	}

	@Override
	public Identifier getKey(V object) {
		return this.invertedMap.get(object);
	}

	@Override
	public V getValue(Object key) {
		return this.defaultMap.get(new Identifier(key));
	}

	@NotNull
	@Override
	public Iterator<V> iterator() {
		return this.defaultMap.values().iterator();
	}

	@Override
	public V register(int i, Object key, V value) {
		return this.register(i, key, value, true);
	}

	private V register(int i, Object key, V value, boolean update) {
		this.defaultMap.put(new Identifier(key), value);
		this.IDLIST.setValue(value, i);
		this.addArrayEntry(i, value, update);
		return value;
	}

	private void addArrayEntry(int i, V value, boolean update) {
		while (i >= this.valueArray.length) {
			this.valueArray = Arrays.copyOf(this.valueArray, this.valueArray.length * 2);
		}

		if (this.valueArray[i] != value) {
			this.valueArray[i] = value;

			if (update) this.updateArray();
		}
	}

	public void syncArrayWithIdList() {
		for (Map.Entry<V, Integer> entry : this.IDLIST.getIdMap(this).entrySet()) {
			this.valueArray[entry.getValue()] = entry.getKey();
		}

		this.updateArray();
	}

	public abstract void updateArray();

	@Override
	public KeyType getKeyType() {
		return KeyType.FABRIC;
	}
}
