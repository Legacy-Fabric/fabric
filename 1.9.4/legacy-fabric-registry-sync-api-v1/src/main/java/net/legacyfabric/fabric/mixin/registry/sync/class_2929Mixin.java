/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

import java.util.IdentityHashMap;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_2929;
import net.minecraft.util.Identifier;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

@Mixin(class_2929.class)
public abstract class class_2929Mixin<V> implements IdListCompat<V> {
	@Shadow
	public abstract void method_12859(V object, int i);

	@Shadow
	@Nullable
	public abstract V method_12857(int i);

	@Shadow
	public abstract int method_12858(V object);

	@Shadow
	private V[] field_14375;

	@Override
	public IdentityHashMap<V, Integer> getIdMap(SimpleRegistryCompat<Identifier, V> simpleRegistry) {
		IdentityHashMap<V, Integer> idMap = new IdentityHashMap<>(512);

		for (int i = 0; i < this.field_14375.length; i++) {
			V value = this.field_14375[i];

			if (value == null) {
				continue;
			}

			idMap.put(value, i);
		}

		return idMap;
	}

	@Override
	public List<V> getList() {
		return null;
	}

	@Override
	public V fromInt(int index) {
		return this.method_12857(index);
	}

	@Override
	public void setValue(V value, int index) {
		this.method_12859(value, index);
	}

	@Override
	public int getInt(V value) {
		return this.method_12858(value);
	}

	@Override
	public IdListCompat<V> createIdList() {
		return (IdListCompat<V>) new class_2929<V>(256);
	}
}
