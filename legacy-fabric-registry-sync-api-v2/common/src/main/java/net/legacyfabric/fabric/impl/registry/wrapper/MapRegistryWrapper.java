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
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.RegistrableRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

public class MapRegistryWrapper<K, V> implements RegistrableRegistry<V> {
	private final Identifier id;
	private final Map<K, V> idToValue;
	private final Map<V, K> valueToId;
	private final Function<Identifier, K> toMapKey;
	private final Function<K, Identifier> fromMapKey;

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

	public MapRegistryWrapper(Identifier id, Map<K, V> idToValue, Map<V, K> valueToId, Function<Identifier, K> toMapKey, Function<K, Identifier> fromMapKey) {
		this.id = id;
		this.idToValue = idToValue;
		this.valueToId = valueToId;
		this.toMapKey = toMapKey;
		this.fromMapKey = fromMapKey;
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
	public K fabric$toKeyType(Identifier identifier) {
		return toMapKey.apply(identifier);
	}

	@Override
	public V fabric$getValue(Identifier id) {
		return this.idToValue.get(this.fabric$toKeyType(id));
	}

	@Override
	public Identifier fabric$getId(V value) {
		K key = this.valueToId.get(value);

		if (key == null) return null;

		return fromMapKey.apply(key);
	}

	@NotNull
	@Override
	public Iterator<V> iterator() {
		return this.idToValue.values().iterator();
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		fabric$getBeforeAddedCallback().invoker().onEntryAdding(rawId, identifier, value);

		this.idToValue.put(this.fabric$toKeyType(identifier), value);
		this.valueToId.put(value, this.fabric$toKeyType(identifier));

		fabric$getEntryAddedCallback().invoker().onEntryAdded(rawId, identifier, value);
	}
}
