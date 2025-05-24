/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.impl.entity;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistrableFabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.IdsHolderImpl;

public class MapEntityRegistryWrapper<V> implements SyncedRegistrableFabricRegistry<V> {
	private final Identifier id = RegistryIds.ENTITY_TYPES;

	private final Event<RegistryEntryAddedCallback<V>> addObjectEvent = EventFactory.createArrayBacked(RegistryEntryAddedCallback.class,
			(callbacks) -> (rawId, id, object) -> {
				for (RegistryEntryAddedCallback<V> callback : callbacks) {
					callback.onEntryAdded(rawId, id, object);
				}
			}
	);
	private final Event<RegistryBeforeAddCallback<V>> beforeAddObjectEvent = EventFactory.createArrayBacked(RegistryBeforeAddCallback.class,
			(callbacks) -> (rawId, id, object) -> {
				for (RegistryBeforeAddCallback<V> callback : callbacks) {
					callback.onEntryAdding(rawId, id, object);
				}
			}
	);
	private final Event<RegistryRemapCallback<V>> remapCallbackEvent = EventFactory.createArrayBacked(RegistryRemapCallback.class,
			(callbacks) -> (changedMap) -> {
				for (RegistryRemapCallback<V> callback : callbacks) {
					callback.callback(changedMap);
				}
			}
	);

	private final Map<String, V> nameToValue;
	private final Map<V, String> valueToName;
	private final Map<Integer, V> idToValue;
	private final Map<V, Integer> valueToId;
	private final Map<String, Integer> nameToId;

	private IdsHolder<V> idsHolder = new IdsHolderImpl<>(1);
	private final BiMap<Identifier, V> keyToValue = HashBiMap.create();
	private final BiMap<V, Identifier> valueToKey = keyToValue.inverse();

	public MapEntityRegistryWrapper(Map<String, V> nameToValue, Map<V, String> valueToName, Map<Integer, V> idToValue, Map<V, Integer> valueToId, Map<String, Integer> nameToId) {
		this.nameToValue = nameToValue;
		this.valueToName = valueToName;
		this.idToValue = idToValue;
		this.valueToId = valueToId;
		this.nameToId = nameToId;

		for (Map.Entry<Integer, V> entry : idToValue.entrySet()) {
			this.idsHolder.fabric$setValue(entry.getValue(), entry.getKey());
		}

		for (Map.Entry<String, V> entry : nameToValue.entrySet()) {
			this.keyToValue.put(EntityHelperImpl.NAME_TO_ID.get(entry.getKey()), entry.getValue());
		}
	}

	@Override
	public int fabric$getRawId(V value) {
		return this.idsHolder.fabric$getId(value);
	}

	@Override
	public V fabric$getValue(int rawId) {
		return this.idsHolder.fabric$getValue(rawId);
	}

	@Override
	public Event<RegistryRemapCallback<V>> fabric$getRegistryRemapCallback() {
		return this.remapCallbackEvent;
	}

	@Override
	public Identifier fabric$getId() {
		return this.id;
	}

	@Override
	public Event<RegistryEntryAddedCallback<V>> fabric$getEntryAddedCallback() {
		return this.addObjectEvent;
	}

	@Override
	public Event<RegistryBeforeAddCallback<V>> fabric$getBeforeAddedCallback() {
		return this.beforeAddObjectEvent;
	}

	@Override
	public Identifier fabric$toKeyType(Identifier identifier) {
		return identifier;
	}

	@Override
	public V fabric$getValue(Identifier id) {
		return this.keyToValue.get(id);
	}

	@Override
	public Identifier fabric$getId(V value) {
		return this.valueToKey.get(value);
	}

	@NotNull
	@Override
	public Iterator<V> iterator() {
		return this.idsHolder.iterator();
	}

	@Override
	public IdsHolder<V> fabric$getIdsHolder() {
		return this.idsHolder;
	}

	@Override
	public void fabric$updateRegistry(IdsHolder<V> ids) {
		this.idsHolder = ids;

		this.idToValue.clear();
		this.valueToId.clear();
		this.nameToId.clear();

		for (Map.Entry<String, V> entry : this.nameToValue.entrySet()) {
			int id = this.idsHolder.fabric$getId(entry.getValue());

			this.idToValue.put(id, entry.getValue());
			this.valueToId.put(entry.getValue(), id);
			this.nameToId.put(entry.getKey(), id);
		}
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		beforeAddObjectEvent.invoker().onEntryAdding(rawId, identifier, value);

		String name = identifier.toTranslationKey();

		nameToValue.put(name, value);
		valueToName.put(value, name);
		idToValue.put(rawId, value);
		valueToId.put(value, rawId);
		nameToId.put(name, rawId);

		keyToValue.put(identifier, value);
		idsHolder.fabric$setValue(value, rawId);

		addObjectEvent.invoker().onEntryAdded(rawId, identifier, value);
	}
}
