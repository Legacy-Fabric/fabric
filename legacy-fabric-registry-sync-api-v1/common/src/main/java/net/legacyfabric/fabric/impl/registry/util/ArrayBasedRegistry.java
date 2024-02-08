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

package net.legacyfabric.fabric.impl.registry.util;

import com.google.common.collect.HashBiMap;

import net.legacyfabric.fabric.api.util.Identifier;

public abstract class ArrayBasedRegistry<V> extends ArrayAndMapBasedRegistry<Identifier, V> {
	public ArrayBasedRegistry(V[] valueArray) {
		super(valueArray, HashBiMap.create());
	}

	@Override
	public void initRegistry(V[] originalValueArray) {
		for (int i = 0; i < originalValueArray.length; i++) {
			V value = originalValueArray[i];
			Identifier key = this.getNewKey(i);

			if (value == null || key == null || this.getObjects().containsKey(value)) continue;

			this.register(i, key, value);
		}
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.FABRIC;
	}
}
