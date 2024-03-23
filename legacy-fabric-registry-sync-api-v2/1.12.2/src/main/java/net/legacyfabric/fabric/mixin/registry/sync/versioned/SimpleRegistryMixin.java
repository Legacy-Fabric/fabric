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
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_2929;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistryHolder;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.SyncedRegistrable;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<K, V> implements SyncedRegistryHolder<V>, SyncedRegistrable<V> {
	@Shadow
	public abstract void add(int id, K identifier, V object);

	@Shadow
	@Final
	protected class_2929<V> field_13718;

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
}
