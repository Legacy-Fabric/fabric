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
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;

@Mixin(class_2929.class)
public abstract class class_2929Mixin<T> implements IdListCompat<T> {
	@Shadow
	public abstract void method_12859(T object, int i);

	@Shadow
	@Nullable
	public abstract T method_12857(int i);

	@Shadow
	public abstract int method_12858(T object);

	@Shadow
	private T[] field_14375;

	@Override
	public IdentityHashMap<T, Integer> getIdMap(SimpleRegistry<Identifier, T> simpleRegistry) {
		IdentityHashMap<T, Integer> idMap = new IdentityHashMap<>(512);

		for (int i = 0; i < this.field_14375.length; i++) {
			T value = this.field_14375[i];

			if (value == null) {
				continue;
			}

			idMap.put(value, i);
		}

		return idMap;
	}

	@Override
	public List<T> getList() {
		return null;
	}

	@Override
	public T fromInt(int index) {
		return this.method_12857(index);
	}

	@Override
	public void setValue(T value, int index) {
		this.method_12859(value, index);
	}

	@Override
	public int getInt(T value) {
		return this.method_12858(value);
	}

	@Override
	public IdListCompat<T> createIdList() {
		return (IdListCompat<T>) new class_2929<T>(256);
	}
}
