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

package net.legacyfabric.fabric.mixin.registry.sync.versioned;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.class_2929;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.SyncedRegistrable;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<K, V> implements SyncedRegistry<V>, SyncedRegistrable<V> {
	// 1.8+
	@Shadow
	public abstract void add(int id, K identifier, V object);

	// 1.9+
	@Mutable
	@Shadow
	@Final
	protected class_2929<V> field_13718;

	// 1.8+
	@Shadow
	public abstract K getIdentifier(Object par1);

	// 1.9+
	@Shadow
	public abstract int getRawId(Object object);

	// 1.7+
	@Shadow
	public abstract Object getByRawId(int index);

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		fabric$getBeforeAddedCallback().invoker().onEntryAdding(rawId, identifier, value);
		add(rawId, fabric$toKeyType(identifier), value);
		fabric$getEntryAddedCallback().invoker().onEntryAdded(rawId, identifier, value);
	}

	@Override
	public IdsHolder<V> fabric$getIdsHolder() {
		return (IdsHolder<V>) field_13718;
	}

	@Override
	public Identifier fabric$getId(V value) {
		return new Identifier(getIdentifier(value));
	}

	@Override
	public int fabric$getRawId(V value) {
		return getRawId(value);
	}

	@Override
	public V fabric$getValue(int rawId) {
		return (V) getByRawId(rawId);
	}

	@Override
	public void fabric$updateRegistry(IdsHolder<V> ids) {
		this.field_13718 = (class_2929<V>) ids;
	}

	@Unique
	private Event<RegistryRemapCallback<V>> fabric_remapCallbackEvent;

	@Override
	public Event<RegistryRemapCallback<V>> fabric$getRegistryRemapCallback() {
		if (this.fabric_remapCallbackEvent == null) {
			this.fabric_remapCallbackEvent = EventFactory.createArrayBacked(RegistryRemapCallback.class,
					(callbacks) -> (changedMap) -> {
						for (RegistryRemapCallback<V> callback : callbacks) {
							callback.callback(changedMap);
						}
					}
			);
		}

		return this.fabric_remapCallbackEvent;
	}
}
