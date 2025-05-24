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

import java.util.function.Function;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.RegistryEntryCreator;
import net.legacyfabric.fabric.api.util.Identifier;

public class RegistryEntryCreatorImpl<T> implements RegistryEntryCreator<T> {
	private final Identifier identifier;
	private final int offset;
	private final Function<Integer, T> valueGetter;

	private T value;

	public RegistryEntryCreatorImpl(Identifier identifier, int offset, Function<Integer, T> valueGetter) {
		this.identifier = identifier;
		this.offset = offset;
		this.valueGetter = valueGetter;
	}

	@Override
	public T getValue(int id) {
		if (this.value == null) {
			this.value = this.valueGetter.apply(id);
		}

		return this.value;
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public int getIdOffset() {
		return this.offset;
	}
}
