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

import java.util.IdentityHashMap;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.collection.IdList;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;

@Mixin(IdList.class)
public abstract class IdListMixinV2<T> implements IdsHolder<T> {
	@Shadow
	public abstract T fromId(int index);

	@Shadow
	public abstract void set(T value, int index);

	@Shadow
	public abstract int getId(T value);

	@Shadow
	@Final
	private IdentityHashMap<T, Integer> idMap;

	@Override
	public IdsHolder<T> fabric$new() {
		return (IdsHolder<T>) new IdList<>();
	}

	@Override
	public int fabric$nextId() {
		int id = 1;

		while (this.fabric$getValue(id) != null) id++;

		return id;
	}

	@Override
	public void fabric$setValue(T value, int id) {
		set(value, id);
	}

	@Override
	public int fabric$size() {
		return idMap.size();
	}

	@Override
	public T fabric$getValue(int rawId) {
		return this.fromId(rawId);
	}

	@Override
	public int fabric$getId(T value) {
		return getId(value);
	}
}
