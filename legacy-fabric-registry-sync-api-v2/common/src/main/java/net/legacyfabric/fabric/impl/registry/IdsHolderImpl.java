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

package net.legacyfabric.fabric.impl.registry;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;

public class IdsHolderImpl<T> implements IdsHolder<T> {
	private final IdentityHashMap<T, Integer> valueToId = new IdentityHashMap<>(512);
	private final List<T> values = Lists.newArrayList();

	private final int minId;

	public IdsHolderImpl(int minId) {
		this.minId = minId;
	}

	public IdsHolderImpl() {
		this(0);
	}

	@Override
	public IdsHolder<T> fabric$new() {
		return new IdsHolderImpl<>(this.minId);
	}

	@Override
	public int fabric$nextId() {
		int id = this.minId;

		while (this.fabric$getValue(id) != null) id++;

		return id;
	}

	@Override
	public void fabric$setValue(T value, int index) {
		this.valueToId.put(value, index);

		while (this.values.size() <= index) {
			this.values.add(null);
		}

		this.values.set(index, value);
	}

	@Override
	public int fabric$size() {
		return this.valueToId.size();
	}

	@Override
	public int fabric$getId(T value) {
		Integer integer = this.valueToId.get(value);
		return integer == null ? -1 : integer;
	}

	@Override
	public T fabric$getValue(int rawId) {
		return rawId >= this.minId && rawId < this.values.size() ? this.values.get(rawId) : null;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return Iterators.filter(this.values.iterator(), Predicates.notNull());
	}
}
