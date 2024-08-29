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

import java.util.Objects;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.registry.BiDefaultedRegistry;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.DesynchronizeableRegistrable;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.SyncedRegistrable;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(BiDefaultedRegistry.class)
public abstract class BiDefaultedRegistryMixin<K, V> implements SyncedRegistry<V>, SyncedRegistrable<V>, DesynchronizeableRegistrable {
	@Shadow
	private V defaultValue;

	@Shadow
	@Final
	private K defaultKey;

	@Override
	public V fabric$getValue(Identifier id) {
		K key = (K) fabric$toKeyType(id);
		V value = ((BiDefaultedRegistry<K, V>) (Object) this).get(key);

		if (value == this.defaultValue && !Objects.equals(this.defaultKey.toString(), key.toString())) return null;

		return value;
	}
}
