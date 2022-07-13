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
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v1.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public class VanillaLikeRegistry<K, V> implements SimpleRegistryCompat<K, V> {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final Map<K, V> map = this.createMap();
	protected IdList<V> ids = new IdList<>();
	protected final Map<V, K> objects = ((BiMap) this.map).inverse();

	private Event<RegistryEntryAddedCallback<V>> entryAddedCallBack = this.createAddEvent();

	public VanillaLikeRegistry() {
	}

	public void put(K key, V value) {
		Validate.notNull(key);
		Validate.notNull(value);

		if (this.map.containsKey(this.toKeyType(key))) {
			LOGGER.debug("Adding duplicate key '{}' to registry", this.toKeyType(key));
		}

		this.map.put(this.toKeyType(key), value);
	}

	protected Map<K, V> createMap() {
		return HashBiMap.create();
	}

	public Iterator<V> iterator() {
		return this.ids.iterator();
	}

	@Override
	public Event<RegistryEntryAddedCallback<V>> getAddEvent() {
		return this.entryAddedCallBack;
	}

	@Override
	public void setAddEvent(Event<RegistryEntryAddedCallback<V>> event) {
		this.entryAddedCallBack = event;
	}

	@Override
	public IdListCompat<V> getIds() {
		return (IdListCompat<V>) this.ids;
	}

	@Override
	public Map<V, K> getObjects() {
		return this.objects;
	}

	@Override
	public void setIds(IdListCompat<V> idList) {
		this.ids = (IdList<V>) idList;
	}

	@Override
	public IdListCompat<V> createIdList() {
		return (IdListCompat<V>) new IdList<V>();
	}

	@Override
	public int getRawID(V object) {
		return this.ids.getId(object);
	}

	@Override
	public K getKey(V id) {
		return this.objects.get(id);
	}

	@Override
	public V getValue(Object key) {
		return this.map.get(this.toKeyType(key));
	}

	@Override
	public V register(int id, Object identifier, V object) {
		this.ids.set(object, id);
		this.put(this.toKeyType(identifier), object);
		this.getAddEvent().invoker().onEntryAdded(id, new Identifier(identifier), object);
		return object;
	}
}
