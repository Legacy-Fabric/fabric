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

@FunctionalInterface
public interface TriConsumer<K, L, R> {
	void accept(K key, L left, R right);

	default TriConsumer<K, L, R> andThen(TriConsumer<? super K, ? super L, ? super R> after) {
		Objects.requireNonNull(after);

		return (k, t, v) -> {
			this.accept(k, t, v);
			after.accept(k, t, v);
		};
	}
}
