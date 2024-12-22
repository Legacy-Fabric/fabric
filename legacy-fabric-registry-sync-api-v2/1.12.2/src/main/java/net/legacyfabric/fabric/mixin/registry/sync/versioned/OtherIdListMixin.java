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

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_2929;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;

@Mixin(class_2929.class)
public abstract class OtherIdListMixin<T> implements IdsHolder<T> {
	@Shadow
	@Nullable
	public abstract T getById(int id);

	@Shadow
	public abstract void add(T value, int id);

	@Shadow
	public abstract int getId(T value);

	@Shadow
	public abstract int size();

	@Override
	public IdsHolder<T> fabric$new() {
		return (IdsHolder<T>) new class_2929<>(256);
	}

	@Override
	public int fabric$nextId() {
		int id = 1;

		while (this.getById(id) != null) id++;

		return id;
	}

	@Override
	public void fabric$setValue(T value, int id) {
		add(value, id);
	}

	@Override
	public int fabric$size() {
		return this.size();
	}

	@Override
	public int fabric$getId(T value) {
		return getId(value);
	}

	@Override
	public T fabric$getValue(int rawId) {
		return this.getById(rawId);
	}
}
