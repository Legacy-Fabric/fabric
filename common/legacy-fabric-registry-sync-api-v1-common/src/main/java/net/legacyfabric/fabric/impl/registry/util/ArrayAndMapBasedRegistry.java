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

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v1.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public abstract class ArrayAndMapBasedRegistry<K, V> implements SimpleRegistryCompat<K, V> {
	private V[] valueArray;
	private final Map<K, V> defaultMap;
	private final Map<V, K> invertedMap;
	private IdListCompat<V> IDLIST = (IdListCompat<V>) new IdList<V>();

	private final Map<K, K> idsMap;
	private final Map<K, K> invertedIdsMap;

	private Event<RegistryEntryAddedCallback<V>> entryAddedCallBack = this.createAddEvent();

	private boolean init = false;

	public ArrayAndMapBasedRegistry(V[] valueArray, BiMap<K, V> defaultMap) {
		this.valueArray = (V[]) Array.newInstance(valueArray.getClass().getComponentType(), valueArray.length + 1);
		Arrays.fill(this.valueArray, null);

		this.defaultMap = defaultMap;
		this.invertedMap = ((BiMap<K, V>) this.defaultMap).inverse();

		this.idsMap = this.getRemapIdList();
		this.invertedIdsMap = ((BiMap<K, K>) this.idsMap).inverse();
		this.remapDefaultIds();

		this.initRegistry(valueArray);
		this.init = true;

		this.syncArrayWithIdList();
	}

	public K getNewId(K oldKey) {
		return this.idsMap.getOrDefault(oldKey, oldKey);
	}

	public K getOldId(K newKey) {
		return this.invertedIdsMap.getOrDefault(newKey, newKey);
	}

	private void remapDefaultIds() {
		for (Map.Entry<K, K> entry : this.idsMap.entrySet()) {
			V value = this.defaultMap.remove(entry.getKey());

			if (value == null) continue;

			if (!this.invertedMap.containsKey(value)) this.defaultMap.put(entry.getValue(), value);
		}
	}

	public void initRegistry(V[] originalValueArray) {
		for (int i = 0; i < originalValueArray.length; i++) {
			V value = originalValueArray[i];

			if (value == null) continue;

			this.register(i,
					this.invertedMap.getOrDefault(value, this.toKeyType(new Identifier("modded", String.valueOf(i)))),
					value
			);
		}
	}

	@Override
	public IdListCompat<V> getIds() {
		return IDLIST;
	}

	@Override
	public Map<V, K> getObjects() {
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
		this.IDLIST.setValue(value, i);

		if (this.init) {
			this.syncArrayWithIdList();
			this.getAddEvent().invoker().onEntryAdded(i, new Identifier(key), value);
		}

		return value;
	}

	public void updateArrayLength(int i) {
		while (i >= this.valueArray.length) {
			this.valueArray = Arrays.copyOf(this.valueArray, this.valueArray.length * 2);
		}

		this.updateArray();
	}

	public void syncArrayWithIdList() {
		Arrays.fill(this.valueArray, null);

		this.updateArrayLength(this.IDLIST.getIdMap(this).size() + 1);

		for (Map.Entry<V, Integer> entry : this.IDLIST.getIdMap(this).entrySet()) {
			this.valueArray[entry.getValue()] = entry.getKey();
		}

		this.updateArray();
	}

	public V[] getArray() {
		return this.valueArray;
	}

	public abstract void updateArray();

	@Override
	public Event<RegistryEntryAddedCallback<V>> getAddEvent() {
		return this.entryAddedCallBack;
	}

	public Map<K, K> getRemapIdList() {
		return HashBiMap.create();
	}

	@Override
	public void setAddEvent(Event<RegistryEntryAddedCallback<V>> event) {
		this.entryAddedCallBack = event;
	}
}
