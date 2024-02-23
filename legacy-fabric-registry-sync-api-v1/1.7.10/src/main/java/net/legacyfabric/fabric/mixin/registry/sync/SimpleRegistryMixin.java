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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.impl.registry.util.RegistryEventsHolder;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<K, V> implements SimpleRegistryCompat<K, V> {
	@Mutable
	@Shadow
	@Final
	protected IdList ids;

	@Shadow
	@Final
	protected Map<V, K> objects;
	private RegistryEventsHolder<V> registryEventsHolder;

	@Shadow
	public abstract Object get(String par1);

	@Shadow
	public abstract int getRawId(Object object);

	@Shadow
	public abstract String getId(Object object);

	@Shadow
	public abstract void add(int rawId, String id, Object object);

	@Override
	public IdListCompat<V> getIds() {
		return (IdListCompat<V>) this.ids;
	}

	@Override
	public Map<V, K> getObjects() {
		return this.objects;
	}

	@Override
	public void setIds(IdListCompat<V> idList) {
		this.ids = (IdList) idList;
	}

	@Override
	public IdListCompat<V> createIdList() {
		return (IdListCompat<V>) new IdList();
	}

	@Override
	public int getRawID(V object) {
		return this.getRawId(object);
	}

	@Override
	public K getKey(V object) {
		return (K) this.getId(object);
	}

	@Override
	public V getValue(Object key) {
		return (V) this.get((String) this.toKeyType(key));
	}

	@Override
	public V register(int i, Object key, V value) {
		this.add(i, (String) this.toKeyType(key), value);
		this.getEventHolder().getAddEvent().invoker().onEntryAdded(i, new Identifier(key), value);
		return value;
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.JAVA;
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
