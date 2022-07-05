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

package net.legacyfabric.fabric.impl.registry.sync.remappers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NbtCompound;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.ArrayBasedRegistry;

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

	public static final Identifier REGISTRY_REMAPPER = new Identifier("legacy-fabric-registry-sync-api-v1-common", "registry_remappers");
	public static final Identifier ITEMS = new Identifier("items");
	public static final Identifier BLOCKS = new Identifier("blocks");
	public static final Identifier BLOCK_ENTITIES = new Identifier("block_entities");

	public static RegistryRemapper<RegistryRemapper<?>> DEFAULT_CLIENT_INSTANCE = null;

	public RegistryRemapper(SimpleRegistryCompat<?, V> registry, Identifier registryId, String type, String nbtName) {
		this.registry = registry;
		this.registryId = registryId;
		this.type = type;
		this.nbtName = nbtName;

		if (this instanceof RegistryRemapperRegistryRemapper && DEFAULT_CLIENT_INSTANCE == null) {
			DEFAULT_CLIENT_INSTANCE = (RegistryRemapper<RegistryRemapper<?>>) this;
		}
	}

	public void dump() {
		this.entryDump = HashBiMap.create();
		RegistryHelperImpl.getIdMap(this.registry).forEach((value, id) -> {
			Object key = RegistryHelperImpl.getObjects(this.registry).get(value);
			if (key != null) this.entryDump.put(new Identifier(key), id);
		});

		this.entryDump.putAll(this.missingMap);
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

	// Type erasure, ily
	public void remap() {
		LOGGER.info("Remapping registry %s", this.registryId.toString());
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

		IntSupplier currentSize = () -> RegistryHelperImpl.getIdMap(newList, this.registry).size();
		IntSupplier previousSize = () -> RegistryHelperImpl.getObjects(this.registry).size();

		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			if (this.missingMap.size() == 0) {
				throw new IllegalStateException("Registry size increased from " + previousSize.getAsInt() + " to " + currentSize.getAsInt() + " after remapping! This is not possible!");
			}
		} else if (currentSize.getAsInt() < previousSize.getAsInt()) {
			LOGGER.info("Adding " + (previousSize.getAsInt() - currentSize.getAsInt()) + " missing entries to registry");

			RegistryHelperImpl.getObjects(this.registry).keySet().stream().filter(obj -> newList.getInt(obj) == -1).forEach(missing -> {
				int id = RegistryHelperImpl.nextId(this.registry);

				while (newList.fromInt(id) != null) {
					id = RegistryHelperImpl.nextId(newList, this.registry);

					V currentBlock = RegistryHelperImpl.getIdList(this.registry).fromInt(id);

					if (currentBlock != null && newList.getInt(currentBlock) == -1) {
						newList.setValue(currentBlock, id);
					}
				}

				if (newList.getInt(missing) == -1) {
					newList.setValue(missing, id);
				} else {
					id = newList.getInt(missing);
				}

				LOGGER.info("Adding %s %s with numerical id %d to registry", this.type, this.registry.getKey(missing), id);
			});
		}

		if (currentSize.getAsInt() != previousSize.getAsInt() && this.missingMap.size() == 0) {
			throw new IllegalStateException("An error occured during remapping");
		}

		this.registry.setIds(newList);

		if (this.registry instanceof ArrayBasedRegistry) {
			((ArrayBasedRegistry) this.registry).syncArrayWithIdList();
		}

		this.dump();
		LOGGER.info("Remapped " + previousSize.getAsInt() + " entries");
	}

	public static <K, V> RegistryRemapper<V> getRegistryRemapper(SimpleRegistryCompat<K, V> simpleRegistry) {
		return (RegistryRemapper<V>) REGISTRY_REMAPPER_MAP.getOrDefault(simpleRegistry, null);
	}

	public static <V> RegistryRemapper<V> getRegistryRemapper(Identifier identifier) {
		return (RegistryRemapper<V>) REMAPPER_MAP.getOrDefault(identifier, null);
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
}
