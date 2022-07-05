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

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public class MapBasedRegistry<K, V> implements SimpleRegistryCompat<K, V> {
	private final Map<K, V> defaultMap;
	private final Map<V, K> invertedMap;

	private IdListCompat<V> IDLIST = (IdListCompat<V>) new IdList<V>();

	private final Map<K, K> idsMap;
	private final Map<K, K> invertedIdsMap;

	public MapBasedRegistry(Map<K, V> defaultMap, Map<V, K> invertedMap) {
		this.defaultMap = defaultMap;
		this.invertedMap = invertedMap;

		this.idsMap = this.getRemapIdList();
		this.invertedIdsMap = ((BiMap<K, K>) this.idsMap).inverse();
		this.remapDefaultIds();
	}

	private void remapDefaultIds() {
		for (Map.Entry<K, K> entry : this.idsMap.entrySet()) {
			V value = this.defaultMap.remove(entry.getKey());

			if (value == null) continue;

			this.defaultMap.put(entry.getValue(), value);
			this.invertedMap.put(value, entry.getValue());
		}
	}

	public K getNewId(K oldKey) {
		return this.idsMap.getOrDefault(oldKey, oldKey);
	}

	public K getOldId(K newKey) {
		return this.invertedIdsMap.getOrDefault(newKey, newKey);
	}

	public Map<K, K> getRemapIdList() {
		return HashBiMap.create();
	}

	@Override
	public IdListCompat<V> getIds() {
		return IDLIST;
	}

	@Override
	public Map<V, K> getObjects() {
		if (this.invertedMap instanceof BiMap) {
			return this.invertedMap;
		}

		return HashBiMap.create(this.invertedMap);
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
	public K getKey(V object) {
		return this.invertedMap.get(object);
	}

	@Override
	public V getValue(Object key) {
		return this.defaultMap.get(this.toKeyType(key));
	}

	@NotNull
	@Override
	public Iterator<V> iterator() {
		return this.IDLIST.iterator();
	}

	@Override
	public V register(int i, Object key, V value) {
		this.defaultMap.put(this.toKeyType(key), value);

		if (!this.invertedMap.containsKey(value)) {
			this.invertedMap.put(value, this.toKeyType(key));
		}

		this.IDLIST.setValue(value, i);
		return value;
	}
}
