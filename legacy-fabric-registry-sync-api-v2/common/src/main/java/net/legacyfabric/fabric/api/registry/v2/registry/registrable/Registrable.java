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

package net.legacyfabric.fabric.api.registry.v2.registry.registrable;

import net.legacyfabric.fabric.api.util.Identifier;

/**
 * A registry-like object that can register new entries.
 * @param <T> type of entries to register
 */
public interface Registrable<T> {
	/**
	 * Register an entry to this registry-like object.
	 * @param rawId The entry's numerical id
	 * @param identifier The entry's identifier
	 * @param value The entry value
	 */
	void fabric$register(int rawId, Identifier identifier, T value);
}
