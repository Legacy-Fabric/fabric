/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.DesynchronizeableRegistrable;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.SyncedRegistrable;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<K, V> implements SyncedFabricRegistry<V>, SyncedRegistrable<V>, DesynchronizeableRegistrable {
	@Shadow
	public abstract void add(int id, K identifier, V object);

	@Shadow
	public abstract K getIdentifier(Object par1);

	@Shadow
	public abstract int getRawId(Object object);

	@Shadow
	public abstract Object getByRawId(int index);

	@Mutable
	@Shadow
	@Final
	protected IdList<V> ids;

	@Unique
	private boolean synchronize = true;

	@Override
	public void fabric$setSynchronize(boolean isSynchronize) {
		this.synchronize = isSynchronize;
	}

	@Override
	public boolean fabric$canSynchronize() {
		return this.synchronize;
	}

	@Override
	public void fabric$register(int rawId, Identifier identifier, V value) {
		fabric$getBeforeAddedCallback().invoker().onEntryAdding(rawId, identifier, value);

		if (this.synchronize) {
			add(rawId, fabric$toKeyType(identifier), value);
		} else {
			((MutableRegistry<K, V>) (Object) this).put(fabric$toKeyType(identifier), value);
		}

		fabric$getEntryAddedCallback().invoker().onEntryAdded(rawId, identifier, value);
	}

	@Override
	public IdsHolder<V> fabric$getIdsHolder() {
		return (IdsHolder<V>) ids;
	}

	@Override
	public Identifier fabric$getId(V value) {
		K vanillaId = getIdentifier(value);

		if (vanillaId == null) return null;

		return new Identifier(vanillaId);
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
		this.ids = (IdList<V>) ids;
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
