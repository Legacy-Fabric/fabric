package net.legacyfabric.fabric.impl.registry.registries;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.Entity;
import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.RegistryEventsHolder;

public class OldEntityTypeRegistry implements SimpleRegistryCompat<String, Class<? extends Entity>> {
	private final BiMap<String, Class<? extends Entity>> stringClassBiMap;
	private final BiMap<Integer, Class<? extends Entity>> integerClassBiMap;
	private final BiMap<String, Integer> stringIntegerBiMap;

	private final Map<String, String> idsMap;

	private RegistryEventsHolder<Class<? extends Entity>> registryEventsHolder;
	private IdListCompat<Class<? extends Entity>> IDLIST = (IdListCompat<Class<? extends Entity>>) new IdList<Class<? extends Entity>>();

	public OldEntityTypeRegistry(BiMap<String, Class<? extends Entity>> stringClassBiMap,
								 BiMap<Integer, Class<? extends Entity>> integerClassBiMap,
								 BiMap<String, Integer> stringIntegerBiMap) {
		this.stringClassBiMap = stringClassBiMap;
		this.integerClassBiMap = integerClassBiMap;
		this.stringIntegerBiMap = stringIntegerBiMap;

		this.idsMap = this.getRemapIdList();

		this.remapDefaultIds();
	}

	private void remapDefaultIds() {
		for (Map.Entry<String, String> entry : this.idsMap.entrySet()) {
			this.stringClassBiMap.put(entry.getValue(), this.stringClassBiMap.remove(entry.getKey()));
			this.stringIntegerBiMap.put(entry.getValue(), this.stringIntegerBiMap.remove(entry.getKey()));
		}

		for (Map.Entry<Integer, Class<? extends Entity>> entry : this.integerClassBiMap.entrySet()) {
			this.IDLIST.setValue(entry.getValue(), entry.getKey());
		}
	}

	public Map<String, String> getRemapIdList() {
		return HashBiMap.create();
	}

	@Override
	public RegistryEventsHolder<Class<? extends Entity>> getEventHolder() {
		return this.registryEventsHolder;
	}

	@Override
	public void setEventHolder(RegistryEventsHolder<Class<? extends Entity>> eventsHolder) {
		this.registryEventsHolder = eventsHolder;
	}

	@Override
	public IdListCompat<Class<? extends Entity>> getIds() {
		return IDLIST;
	}

	@Override
	public Map<Class<? extends Entity>, String> getObjects() {
		return this.stringClassBiMap.inverse();
	}

	@Override
	public void setIds(IdListCompat<Class<? extends Entity>> idList) {
		this.IDLIST = idList;

		BiMap<Class<? extends Entity>, Integer> inversedMap = this.integerClassBiMap.inverse();
		BiMap<Class<? extends Entity>, String> inversedMap2 = this.stringClassBiMap.inverse();

		for (Class<? extends Entity> entityType : this.IDLIST.getList()) {
			inversedMap.put(entityType, this.IDLIST.getInt(entityType));

			this.stringIntegerBiMap.put(inversedMap2.get(entityType), this.IDLIST.getInt(entityType));
		}
	}

	@Override
	public IdListCompat<Class<? extends Entity>> createIdList() {
		return (IdListCompat<Class<? extends Entity>>) new IdList<Class<? extends Entity>>();
	}

	@Override
	public int getRawID(Class<? extends Entity> object) {
		return IDLIST.getInt(object);
	}

	@Override
	public String getKey(Class<? extends Entity> object) {
		return this.stringClassBiMap.inverse().get(object);
	}

	@Override
	public Class<? extends Entity> getValue(Object key) {
		return this.stringClassBiMap.get(this.toKeyType(key));
	}

	@Override
	public Class<? extends Entity> register(int i, Object key, Class<? extends Entity> value) {
		String nativeKey = this.toKeyType(key);

		this.stringClassBiMap.put(nativeKey, value);
		this.integerClassBiMap.put(i, value);
		this.stringIntegerBiMap.put(nativeKey, i);

		this.IDLIST.setValue(value, i);
		this.getEventHolder().getAddEvent().invoker().onEntryAdded(i, new Identifier(key), value);
		return value;
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.JAVA;
	}

	@NotNull
	@Override
	public Iterator<Class<? extends Entity>> iterator() {
		return this.IDLIST.iterator();
	}
}
