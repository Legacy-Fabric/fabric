/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

import java.util.Objects;
import java.util.function.IntSupplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.mixin.registry.sync.SimpleRegistryAccessor;

public class RegistryRemapper {
	protected static final Logger LOGGER = LogManager.getLogger();
	protected final SimpleRegistry<Identifier, ?> registry;
	protected BiMap<Identifier, Integer> entryDump;

	public RegistryRemapper(SimpleRegistry<Identifier, ?> registry) {
		this.registry = registry;
	}

	public void dump() {
		this.entryDump = HashBiMap.create();
		RegistryHelperImpl.getIdMap(this.registry).forEach((value, id) -> {
			Identifier key = RegistryHelperImpl.getObjects(this.registry).get(value);
			this.entryDump.put(key, id);
		});
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
		LOGGER.info("Remapping registry");
		IdList newList = new IdList<>();
		this.entryDump.forEach((id, rawId) -> {
			Object value = Objects.requireNonNull(RegistryHelperImpl.getObjects(this.registry).inverse().get(id));
			newList.set(value, rawId);
		});
		IntSupplier currentSize = () -> RegistryHelperImpl.getIdMap(newList).size();
		IntSupplier previousSize = () -> RegistryHelperImpl.getObjects(this.registry).size();

		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			throw new IllegalStateException("Registry size increased from " + previousSize + " to " + currentSize + " after remapping! This is not possible!");
		} else if (currentSize.getAsInt() < previousSize.getAsInt()) {
			LOGGER.info("Adding " + (previousSize.getAsInt() - currentSize.getAsInt()) + " missing entries to registry");
			RegistryHelperImpl.getObjects(this.registry).keySet().stream().filter(obj -> newList.getId(obj) == -1).forEach(missing -> {
				int id = RegistryHelperImpl.nextId(this.registry);
				newList.set(missing, id);
			});
		}

		if (currentSize.getAsInt() != previousSize.getAsInt()) {
			throw new IllegalStateException("An error occured during remapping");
		}

		((SimpleRegistryAccessor) this.registry).setIds(newList);
		this.dump();
		LOGGER.info("Remapped " + previousSize.getAsInt() + " entries");
	}

	public int nextId() {
		int id = 0;

		while (RegistryHelperImpl.getIdList(this.registry).fromId(id) != null) {
			id++;
		}

		return id;
	}
}
