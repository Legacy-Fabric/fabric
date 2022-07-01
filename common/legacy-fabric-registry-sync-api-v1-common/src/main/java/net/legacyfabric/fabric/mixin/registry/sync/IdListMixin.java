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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;

@Mixin(IdList.class)
public abstract class IdListMixin<T> implements IdListCompat<T> {
	@Shadow
	@Final
	private IdentityHashMap<T, Integer> idMap;

	@Shadow
	@Final
	private List<T> list;

	@Shadow
	public abstract T fromId(int index);

	@Shadow
	public abstract void set(T value, int index);

	@Shadow
	public abstract int getId(T value);

	@Override
	public IdentityHashMap<T, Integer> getIdMap(SimpleRegistry<Identifier, T> simpleRegistry) {
		return this.idMap;
	}

	@Override
	public List<T> getList() {
		return this.list;
	}

	@Override
	public T fromInt(int index) {
		return this.fromId(index);
	}

	@Override
	public void setValue(T value, int index) {
		this.set(value, index);
	}

	@Override
	public int getInt(T value) {
		return this.getId(value);
	}

	@Override
	public IdListCompat<T> createIdList() {
		return (IdListCompat<T>) new IdList<T>();
	}
}
