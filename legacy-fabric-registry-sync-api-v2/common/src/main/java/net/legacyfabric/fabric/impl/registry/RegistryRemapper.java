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

import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NbtCompound;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class RegistryRemapper<T> {
	protected static final Logger LOGGER = Logger.get(LoggerImpl.API, "RegistryRemapper");
	private final SyncedRegistry<T> registry;
	private BiMap<Identifier, Integer> entryDump;
	private BiMap<Identifier, Integer> missingMap = HashBiMap.create();

	public RegistryRemapper(SyncedRegistry<T> registry) {
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
	}
}
