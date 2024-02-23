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

package net.legacyfabric.fabric.impl.registry.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public abstract class OldRemappedRegistry<K, V> implements SimpleRegistryCompat<K, V> {
	private final BiMap<K, K> oldToNewKeyMap = this.generateOldToNewKeyMap();
	private final BiMap<Integer, K> idToKeyMap = this.generateIdToKeyMap();

	private RegistryEventsHolder<V> registryEventsHolder;

	public OldRemappedRegistry() {
	}

	public void remapDefaultIds() {
	}

	public BiMap<K, K> generateOldToNewKeyMap() {
		return HashBiMap.create();
	}

	public BiMap<Integer, K> generateIdToKeyMap() {
		return HashBiMap.create();
	}

	public K getNewKey(K oldKey) {
		return this.oldToNewKeyMap.getOrDefault(oldKey, oldKey);
	}

	public K getOldKey(K newKey) {
		return this.oldToNewKeyMap.inverse().getOrDefault(newKey, newKey);
	}

	public K getNewKey(int oldId) {
		return this.idToKeyMap.getOrDefault(oldId, null);
	}

	public int getOldId(K newKey) {
		return this.idToKeyMap.inverse().getOrDefault(newKey, -1);
	}

	@Override
	public RegistryEventsHolder<V> getEventHolder() {
		return this.registryEventsHolder;
	}

	@Override
	public void setEventHolder(RegistryEventsHolder<V> registryEventsHolder) {
		this.registryEventsHolder = registryEventsHolder;
	}
}
