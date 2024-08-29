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

package net.legacyfabric.fabric.impl.registry;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.util.Identifier;

class RegistryEntryImpl<T> implements RegistryEntry<T> {
	private final int id;
	private final Identifier identifier;
	private final T value;

	RegistryEntryImpl(int id, Identifier identifier, T value) {
		this.id = id;
		this.identifier = identifier;
		this.value = value;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public T getValue() {
		return this.value;
	}
}
