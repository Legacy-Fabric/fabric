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

package net.legacyfabric.fabric.impl.registry.sync.remappers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NbtCompound;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.ArrayBasedRegistry;
import net.legacyfabric.fabric.impl.registry.util.RegistryEventsHolder;

public class RegistryRemapper<V> {
	protected static final Logger LOGGER = Logger.get(LoggerImpl.API, "RegistryRemapper");
	protected final SimpleRegistryCompat<?, V> registry;
	protected BiMap<Identifier, Integer> entryDump;
	protected BiMap<Identifier, Integer> missingMap = HashBiMap.create();
	public final Identifier registryId;
	public final String type;
	public final String nbtName;

	public static final Map<SimpleRegistryCompat<?, ?>, RegistryRemapper<?>> REGISTRY_REMAPPER_MAP = new HashMap<>();
	public static final Map<Identifier, RegistryRemapper<?>> REMAPPER_MAP = new HashMap<>();

	public static RegistryRemapper<RegistryRemapper<?>> DEFAULT_CLIENT_INSTANCE = null;

	private static final Map<Identifier, RegistryEventsHolder<?>> IDENTIFIER_EVENT_MAP = new HashMap<>();

	public RegistryRemapper(SimpleRegistryCompat<?, V> registry, Identifier registryId, String type, String nbtName) {
		this.registry = registry;
		this.registryId = registryId;
		this.type = type;
		this.nbtName = nbtName;

		if (this instanceof RegistryRemapperRegistryRemapper && DEFAULT_CLIENT_INSTANCE == null) {
			DEFAULT_CLIENT_INSTANCE = (RegistryRemapper<RegistryRemapper<?>>) this;
		}

		if (IDENTIFIER_EVENT_MAP.containsKey(this.registryId)) {
			this.registry.setEventHolder((RegistryEventsHolder<V>) IDENTIFIER_EVENT_MAP.get(this.registryId));
		} else {
			this.registry.setEventHolder(new RegistryEventsHolder<>());
		}

		if (RegistryHelper.IDENTIFIER_EVENT_MAP.containsKey(registryId)) {
			RegistryHelper.IDENTIFIER_EVENT_MAP.remove(registryId).invoker();
		}
	}

	public void dump() {
		this.entryDump = HashBiMap.create();
		LOGGER.debug("Dumping registry %s.", this.registryId);
		RegistryHelperImpl.getIdMap(this.registry).forEach((value, id) -> {
			Object key = RegistryHelperImpl.getObjects(this.registry).get(value);
			LOGGER.debug("%s %s %d %s", this.type, key, id, value);
			if (key != null) this.entryDump.put(new Identifier(key), id);
		});

		for (Map.Entry<Identifier, Integer> entry : this.missingMap.entrySet()) {
			if (!this.entryDump.containsValue(entry.getValue())) {
				this.entryDump.put(entry.getKey(), entry.getValue());
			} else {
				LOGGER.warn("Tried to add missing entry %s at index %d, but it is already taken by %s",
						entry.getKey(), entry.getValue(), this.entryDump.inverse().get(entry.getValue()));
			}
		}
	}

	public NbtCompound toNbt() {
		if (this.entryDump == null) {
			this.dump();
		}

		NbtCompound nbt = new NbtCompound();
		this.entryDump.forEach((key, value) -> nbt.putInt(key.toString(), value));
		return nbt;
	}

	public void readNbt(NbtCompound tag) {
		this.entryDump = HashBiMap.create();

		for (String key : tag.getKeys()) {
			Identifier identifier = new Identifier(key);
			int id = tag.getInt(key);
			this.entryDump.put(identifier, id);
		}
	}

	private IdListCompat<V> getExistingFromDump() {
		IdListCompat<V> newList = this.registry.createIdList();

		this.entryDump.forEach((id, rawId) -> {
			V value = RegistryHelperImpl.getObjects(this.registry).inverse().get(this.registry.toKeyType(id));

			if (value == null) {
				newList.setValue(null, rawId);
				LOGGER.warn("%s with id %s is missing!", this.type, id.toString());
				this.missingMap.put(id, rawId);
			} else {
				newList.setValue(value, rawId);
			}
		});

		return newList;
	}

	private void addNewEntries(IdListCompat<V> newList, IntSupplier currentSize, IntSupplier previousSize) {
		LOGGER.info("Adding " + (previousSize.getAsInt() - currentSize.getAsInt()) + " missing entries to registry");

		RegistryHelperImpl.getObjects(this.registry).keySet().stream().filter(obj -> newList.getInt(obj) == -1).forEach(missingValue -> {
			int index = RegistryHelperImpl.nextId(this.registry);

			while (newList.fromInt(index) != null || this.missingMap.containsValue(index)) {
				index = RegistryHelperImpl.nextId(newList, this.registry, this.missingMap);

				V currentValue = RegistryHelperImpl.getIdList(this.registry).fromInt(index);

				if (currentValue == null) {
					break;
				}
			}

			if (newList.getInt(missingValue) == -1) {
				newList.setValue(missingValue, index);
			} else {
				index = newList.getInt(missingValue);
			}

			LOGGER.info("Adding %s %s with numerical id %d to registry", this.type, this.registry.getKey(missingValue), index);
		});
	}

	private void invokeRemapListeners(IdListCompat<V> newList) {
		for (V value : newList) {
			int oldId = this.registry.getIds().getInt(value);
			int newId = newList.getInt(value);

			if (oldId != -1 && oldId != newId) {
				LOGGER.info("Remapped %s %s from id %d to id %d", this.type, this.registry.getKey(value), oldId, newId);
				this.registry.getEventHolder().getRemapEvent().invoker().onEntryAdded(oldId, newId, new Identifier(this.registry.getKey(value)), value);
			}
		}
	}

	private void updateRegistry(IdListCompat<V> newList) {
		this.registry.setIds(newList);

		if (this.registry instanceof ArrayBasedRegistry) {
			((ArrayBasedRegistry<?>) this.registry).syncArrayWithIdList();
		}
	}

	private IntSupplier normalizeRegistryEntryList(IdListCompat<V> newList) {
		IntSupplier currentSize = () -> RegistryHelperImpl.getIdMap(newList, this.registry).size();
		IntSupplier previousSize = () -> RegistryHelperImpl.getObjects(this.registry).size();

		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			if (this.missingMap.isEmpty()) {
				throw new IllegalStateException("Registry size increased from " + previousSize.getAsInt() + " to " + currentSize.getAsInt() + " after remapping! This is not possible!");
			}
		} else if (currentSize.getAsInt() < previousSize.getAsInt()) {
			addNewEntries(newList, currentSize, previousSize);
		}

		if (currentSize.getAsInt() != previousSize.getAsInt() && this.missingMap.isEmpty()) {
			throw new IllegalStateException("An error occured during remapping");
		}

		return previousSize;
	}

	// Type erasure, ily
	public void remap() {
		LOGGER.info("Remapping registry %s", this.registryId.toString());

		if (this.entryDump == null || this.entryDump.isEmpty()) {
			this.dump();
		}

		IdListCompat<V> newList = getExistingFromDump();

		IntSupplier previousSize = normalizeRegistryEntryList(newList);

		invokeRemapListeners(newList);

		updateRegistry(newList);

		this.dump();
		LOGGER.info("Remapped " + previousSize.getAsInt() + " entries");
	}

	public static <K, V> RegistryRemapper<V> getRegistryRemapper(SimpleRegistryCompat<K, V> simpleRegistry) {
		return (RegistryRemapper<V>) REGISTRY_REMAPPER_MAP.getOrDefault(simpleRegistry, null);
	}

	public static <V> RegistryRemapper<V> getRegistryRemapper(Identifier identifier) {
		RegistryRemapper<V> remapper = (RegistryRemapper<V>) REMAPPER_MAP.getOrDefault(identifier, null);
		return remapper == null ? (RegistryRemapper<V>) DEFAULT_CLIENT_INSTANCE : remapper;
	}

	public static <V> RegistryEventsHolder<V> getEventsHolder(Identifier identifier) {
		RegistryRemapper<V> remapper = (RegistryRemapper<V>) REMAPPER_MAP.getOrDefault(identifier, null);

		RegistryEventsHolder<V> event;

		if (remapper == null) {
			if (IDENTIFIER_EVENT_MAP.containsKey(identifier)) {
				event = (RegistryEventsHolder<V>) IDENTIFIER_EVENT_MAP.get(identifier);
			} else {
				event = new RegistryEventsHolder<V>();
				IDENTIFIER_EVENT_MAP.put(identifier, event);
			}
		} else {
			event = remapper.getRegistry().getEventHolder();
		}

		return event;
	}

	public void addMissing(Identifier key, int id) {
		this.missingMap.put(key, id);
	}

	public V register(int i, Object key, V value) {
		return this.registry.register(i, key, value);
	}

	public SimpleRegistryCompat<?, V> getRegistry() {
		return this.registry;
	}

	public Object toKeyType(Object id) {
		return this.registry.toKeyType(id);
	}

	public Identifier getIdentifier(V object) {
		return new Identifier(this.registry.getKey(object).toString());
	}

	public int getMinId() {
		return 0;
	}
}
