package net.legacyfabric.fabric.impl.registry.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

public abstract class OldRemappedRegistry<K, V> implements SimpleRegistryCompat<K, V> {
	private final BiMap<K, K> oldToNewKeyMap = this.generateOldToNewKeyMap();
	private final BiMap<Integer, K> idToKeyMap = this.generateIdToKeyMap();

	private RegistryEventsHolder<V> registryEventsHolder;

	public OldRemappedRegistry() {}

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
