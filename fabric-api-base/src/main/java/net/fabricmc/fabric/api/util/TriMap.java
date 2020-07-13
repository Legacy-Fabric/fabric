/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.util;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

public class TriMap<K, L, R> {
	private final Set<Entry<K, L, R>> entries = Sets.newHashSet();

	private void add(K key, L left, R right) {
		this.entries.add(new Entry<>(key, left, right));
	}

	public void remove(K key) {
		entries.remove(key);
	}

	public void forEach(TriConsumer<K, L, R> action) {
		Objects.requireNonNull(action);

		for (Entry<K, L, R> entry : entries) {
			action.accept(entry.key, entry.left, entry.right);
		}
	}

	private static class Entry<K, L, R> {
		private final K key;
		private final L left;
		private final R right;

		private Entry(K k, L l, R r) {
			this.key = k;
			this.left = l;
			this.right = r;
		}
	}
}
