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

package net.legacyfabric.fabric.impl.registry.wrapper;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.BiMap;
import org.jetbrains.annotations.NotNull;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistrableRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.IdsHolderImpl;

public class SyncedArrayMapRegistryWrapper<K, V> implements SyncedRegistrableRegistry<V> {
	private IdsHolder<V> idsHolder = new IdsHolderImpl<>(1);

	private final Identifier id;
	private final BiMap<K, V> keyToValue;
	private final boolean updateMap;
	private final BiMap<V, K> valueToKey;
	private final Function<Identifier, K> toMapKey;
	private final Function<K, Identifier> fromMapKey;
	private final Consumer<IdsHolder<V>> arraySetter;

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

	public SyncedArrayMapRegistryWrapper(Identifier id, V[] array, BiMap<K, V> keyToValue, boolean updateMap, Function<Identifier, K> toMapKey, Function<K, Identifier> fromMapKey, Consumer<IdsHolder<V>> arraySetter) {
		this.id = id;
		this.keyToValue = keyToValue;
		this.updateMap = updateMap;
		this.valueToKey = this.keyToValue.inverse();
		this.toMapKey = toMapKey;
		this.fromMapKey = fromMapKey;
		this.arraySetter = arraySetter;

		for (int i = 0; i < array.length; i++) {
			V value = array[i];

			if (value != null) {
				idsHolder.fabric$setValue(value, i);
			}
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
		return remapCallbackEvent;
	}

	@Override
	public Identifier fabric$getId() {
		return this.id;
	}

	@Override
	public Event<RegistryEntryAddedCallback<V>> fabric$getEntryAddedCallback() {
		return addObjectEvent;
	}

	@Override
	public Event<RegistryBeforeAddCallback<V>> fabric$getBeforeAddedCallback() {
		return beforeAddObjectEvent;
	}

	@Override
	public K fabric$toKeyType(Identifier identifier) {
		return toMapKey.apply(identifier);
	}

	@Override
	public V fabric$getValue(Identifier id) {
		return this.keyToValue.get(this.fabric$toKeyType(id));
	}

	@Override
	public Identifier fabric$getId(V value) {
		K key = this.valueToKey.get(value);

		if (key == null) return null;

		return fromMapKey.apply(key);
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

		this.arraySetter.accept(this.idsHolder);
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		beforeAddObjectEvent.invoker().onEntryAdding(rawId, identifier, value);

		this.idsHolder.fabric$setValue(value, rawId);
		if (updateMap) this.keyToValue.put(this.fabric$toKeyType(identifier), value);

		addObjectEvent.invoker().onEntryAdded(rawId, identifier, value);
	}
}
