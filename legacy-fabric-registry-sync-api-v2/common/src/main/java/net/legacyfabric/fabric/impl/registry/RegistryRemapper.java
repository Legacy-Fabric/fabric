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

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NbtCompound;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistrableRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class RegistryRemapper<T> {
	protected static final Logger LOGGER = Logger.get(LoggerImpl.API, "RegistryRemapper");
	private final SyncedRegistrableRegistry<T> registry;
	private BiMap<Identifier, Integer> entryDump;
	private BiMap<Identifier, Integer> missingMap = HashBiMap.create();

	public RegistryRemapper(SyncedRegistrableRegistry<T> registry) {
		this.registry = registry;
	}

	public void dump() {
		if (this.entryDump != null && !this.entryDump.isEmpty()) return;

		this.entryDump = HashBiMap.create();

		LOGGER.debug("Dumping registry %s.", this.registry.fabric$getId());

		this.registry.stream()
				.collect(Collectors.toMap(
						this.registry::fabric$getId,
						this.registry::fabric$getRawId
						))
				.entrySet()
				.stream()
				.filter(entry -> entry.getKey() != null && entry.getValue() != -1)
				.forEach(entry -> {
					T value = this.registry.fabric$getValue(entry.getKey());
					LOGGER.debug("[%s] %s %s %s", this.registry.fabric$getId(), entry.getKey(), entry.getValue(), value);
					this.entryDump.put(entry.getKey(), entry.getValue());
				});
	}

	public NbtCompound toNbt() {
		this.dump();

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

	public void remap() {
		LOGGER.info("Remapping registry %s", this.registry.fabric$getId());

		this.dump();

		IdsHolder<T> dumpIds = getDumpIds();

		IntSupplier previousSize = normalizeEntryList(dumpIds);

		invokeListeners(dumpIds);

		updateRegistry(dumpIds);

		this.entryDump.clear();
		this.dump();
		LOGGER.info("Remapped " + previousSize.getAsInt() + " entries");
	}

	private IdsHolder<T> getDumpIds() {
		IdsHolder<T> ids = this.registry.fabric$getIdsHolder().fabric$new();

		this.entryDump.forEach((id, rawId) -> {
			T value = this.registry.fabric$getValue(id);

			if (value == null) {
				LOGGER.warn("[%s] Missing entry %s with raw id %s", this.registry.fabric$getId(), id, rawId);
				this.missingMap.put(id, rawId);
			} else {
				ids.fabric$setValue(value, rawId);
			}
		});

		return ids;
	}

	private IntSupplier normalizeEntryList(IdsHolder<T> ids) {
		IntSupplier currentSize = ids::fabric$size;
		IntSupplier previousSize = () -> this.registry.fabric$getIdsHolder().fabric$size();

		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			if (this.missingMap.isEmpty()) {
				throw new IllegalStateException("Registry size increased from " + previousSize.getAsInt() + " to " + currentSize.getAsInt() + " after remapping! This is not possible!");
			}
		}

		addNewEntries(ids);

		if (currentSize.getAsInt() != previousSize.getAsInt() && this.missingMap.isEmpty()) {
			throw new IllegalStateException("An error occured during remapping");
		}

		return previousSize;
	}

	private void addNewEntries(IdsHolder<T> newList) {
		LOGGER.info("Checking for missing entries in registry");

		this.registry.stream()
				.filter(obj -> !newList.fabric$contains(obj))
				.collect(Collectors.toList())
				.forEach(missingEntry -> {
					int newId = newList.fabric$nextId();

					newList.fabric$setValue(missingEntry, newId);

					LOGGER.info("Adding %s with numerical id %d to registry %s", this.registry.fabric$getId(missingEntry), newId, this.registry.fabric$getId());
				});
	}

	private void invokeListeners(IdsHolder<T> ids) {
		Map<Integer, RegistryEntry<T>> changed = new HashMap<>();

		for (T value : ids) {
			int oldId = this.registry.fabric$getIdsHolder().fabric$getId(value);
			int newId = ids.fabric$getId(value);

			if (oldId != -1 && oldId != newId) {
				LOGGER.info("Remapped %s %s from id %d to id %d", this.registry.fabric$getId(), this.registry.fabric$getId(value), oldId, newId);
				changed.put(oldId, new RegistryEntryImpl<>(newId, this.registry.fabric$getId(value), value));
			}
		}

		this.registry.fabric$getRegistryRemapCallback().invoker().callback(changed);
	}

	private void updateRegistry(IdsHolder<T> ids) {
		this.registry.fabric$updateRegistry(ids);
	}

	private static class RegistryEntryImpl<T> implements RegistryEntry<T> {
		private final int id;
		private final Identifier identifier;
		private final T value;

		RegistryEntryImpl(int id, Identifier identifier, T value) {
			this.id = id;
			this.identifier = identifier;
			this.value = value;
		}

		@Override
		public int getId() {
			return this.id;
		}

		@Override
		public Identifier getIdentifier() {
			return this.identifier;
		}

		@Override
		public T getValue() {
			return this.value;
		}
	}
}
