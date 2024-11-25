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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.util.registry.MutableRegistry;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.Registrable;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;
import net.legacyfabric.fabric.impl.registry.accessor.RegistryIdSetter;

@Mixin(MutableRegistry.class)
public abstract class MutableRegistryMixinV2<K, V> implements FabricRegistry<V>, RegistryIdSetter, Registrable<V> {
	@Shadow
	public abstract void put(Object key, Object value);

	@Shadow
	public abstract Object get(Object key);

	@Shadow
	@Final
	protected Map<K, V> map;
	@Unique
	private Event<RegistryEntryAddedCallback<V>> fabric_addObjectEvent;

	@Unique
	private Event<RegistryBeforeAddCallback<V>> fabric_beforeAddObjectEvent;

	@Unique
	private Identifier fabric_id;

	@Override
	public Event<RegistryEntryAddedCallback<V>> fabric$getEntryAddedCallback() {
		if (this.fabric_addObjectEvent == null) {
			fabric_addObjectEvent = EventFactory.createArrayBacked(RegistryEntryAddedCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryEntryAddedCallback<V> callback : callbacks) {
							callback.onEntryAdded(rawId, id, object);
						}
					}
			);
		}

		return this.fabric_addObjectEvent;
	}

	@Override
	public Event<RegistryBeforeAddCallback<V>> fabric$getBeforeAddedCallback() {
		if (this.fabric_beforeAddObjectEvent == null) {
			fabric_beforeAddObjectEvent = EventFactory.createArrayBacked(RegistryBeforeAddCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryBeforeAddCallback<V> callback : callbacks) {
							callback.onEntryAdding(rawId, id, object);
						}
					}
			);
		}

		return this.fabric_beforeAddObjectEvent;
	}

	@Override
	public Identifier fabric$getId() {
		return this.fabric_id;
	}

	@Override
	public void fabric$setId(Identifier identifier) {
		assert this.fabric_id == null;
		this.fabric_id = identifier;
	}

	@Override
	public K fabric$toKeyType(Identifier identifier) {
		return RegistryHelperImplementation.hasFlatteningBegun ? (K) new net.minecraft.util.Identifier(identifier.toString()) : (K) identifier.toString();
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		fabric$getBeforeAddedCallback().invoker().onEntryAdding(rawId, identifier, value);
		put(fabric$toKeyType(identifier), value);
		fabric$getEntryAddedCallback().invoker().onEntryAdded(rawId, identifier, value);
	}

	@Override
	public V fabric$getValue(Identifier id) {
		return (V) get(fabric$toKeyType(id));
	}

	@Override
	public Identifier fabric$getId(V value) {
		return map.entrySet()
				.stream()
				.filter(entry -> entry.getValue().equals(value))
				.findFirst()
				.map(entry -> new Identifier(entry.getKey()))
				.orElse(null);
	}
}
