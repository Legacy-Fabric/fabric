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

package net.legacyfabric.fabric.impl.registry;

import java.util.Iterator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistrableRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;

public class SyncedRegistrableRegistryImpl<V> implements SyncedRegistrableRegistry<V> {
	private static final Logger LOGGER = Logger.get("LegacyFabricAPI", "SyncedRegistryImpl");
	protected final BiMap<Identifier, V> valueMap = HashBiMap.create();
	protected IdsHolder<V> idsHolder = new IdsHolderImpl<>();
	protected final BiMap<V, Identifier> idMap = valueMap.inverse();

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

	private final Identifier identifier;

	public SyncedRegistrableRegistryImpl(Identifier id) {
		this.identifier = id;
	}

	private void put(Identifier key, V value) {
		Validate.notNull(key);
		Validate.notNull(value);

		if (this.valueMap.containsKey(key)) {
			LOGGER.debug("Adding duplicate key '" + key + "' to registry");
		}

		this.valueMap.put(key, value);
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
		return this.identifier;
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
	public Identifier fabric$toKeyType(Identifier identifier) {
		return identifier;
	}

	@Override
	public V fabric$getValue(Identifier id) {
		return valueMap.get(id);
	}

	@Override
	public Identifier fabric$getId(V value) {
		return idMap.get(value);
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
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		beforeAddObjectEvent.invoker().onEntryAdding(rawId, identifier, value);

		this.idsHolder.fabric$setValue(value, rawId);
		this.put(identifier, value);

		addObjectEvent.invoker().onEntryAdded(rawId, identifier, value);
	}
}
