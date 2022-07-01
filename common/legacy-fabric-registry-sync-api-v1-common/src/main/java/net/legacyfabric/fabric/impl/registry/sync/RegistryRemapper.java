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

package net.legacyfabric.fabric.impl.registry.sync;

import java.util.function.IntSupplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.mixin.registry.sync.SimpleRegistryAccessor;

public class RegistryRemapper<T> {
	protected static final Logger LOGGER = Logger.get(LoggerImpl.API, "RegistryRemapper");
	protected final SimpleRegistry<Identifier, T> registry;
	protected BiMap<Identifier, Integer> entryDump;
	protected BiMap<Identifier, Integer> missingMap = HashBiMap.create();
	protected final Identifier registryId;

	public static final Identifier ITEMS = new Identifier("items");
	public static final Identifier BLOCKS = new Identifier("blocks");

	public RegistryRemapper(SimpleRegistry<Identifier, T> registry, Identifier registryId) {
		this.registry = registry;
		this.registryId = registryId;
	}

	public void dump() {
		this.entryDump = HashBiMap.create();
		RegistryHelperImpl.getIdMap(this.registry).forEach((value, id) -> {
			Identifier key = RegistryHelperImpl.getObjects(this.registry).get(value);
			if (key != null) this.entryDump.put(key, id);
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
		IdList<T> newList = new IdList<>();

		this.entryDump.forEach((id, rawId) -> {
			T value = RegistryHelperImpl.getObjects(this.registry).inverse().get(id);

			if (value == null) {
				newList.set(null, rawId);
				LOGGER.warn("Object with id %s is missing!", id.toString());
				this.missingMap.put(id, rawId);
			} else {
				newList.set(value, rawId);
			}
		});

		IntSupplier currentSize = () -> RegistryHelperImpl.getIdMap(newList).size();
		IntSupplier previousSize = () -> RegistryHelperImpl.getObjects(this.registry).size();

		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			if (this.missingMap.size() == 0) {
				throw new IllegalStateException("Registry size increased from " + previousSize.getAsInt() + " to " + currentSize.getAsInt() + " after remapping! This is not possible!");
			}
		} else if (currentSize.getAsInt() < previousSize.getAsInt()) {
			LOGGER.info("Adding " + (previousSize.getAsInt() - currentSize.getAsInt()) + " missing entries to registry");

			RegistryHelperImpl.getObjects(this.registry).keySet().stream().filter(obj -> newList.getId(obj) == -1).forEach(missing -> {
				int id = RegistryHelperImpl.nextId(this.registry);

				while (newList.fromId(id) != null) {
					id = RegistryHelperImpl.nextId(newList);

					T currentBlock = RegistryHelperImpl.getIdList(this.registry).fromId(id);

					if (currentBlock != null && newList.getId(currentBlock) == -1) {
						newList.set(currentBlock, id);
					}
				}

				if (newList.getId(missing) == -1) {
					newList.set(missing, id);
				} else {
					id = newList.getId(missing);
				}

				LOGGER.info("Adding object %s with numerical id %d to registry", this.registry.getIdentifier(missing), id);
			});
		}

		if (currentSize.getAsInt() != previousSize.getAsInt() && this.missingMap.size() == 0) {
			throw new IllegalStateException("An error occured during remapping");
		}

		((SimpleRegistryAccessor) this.registry).setIds(newList);
		this.dump();
		LOGGER.info("Remapped " + previousSize.getAsInt() + " entries");
	}
}
