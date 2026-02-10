/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

/**
 * A registry-like object that can register new entries and makes use of numerical ids and thus can require remapping.
 * @param <T> type of entries to register
 */
public interface SyncedRegistrable<T> extends Registrable<T> {
	/**
	 * @return This registry-like's {@link IdsHolder} instance
	 */
	IdsHolder<T> fabric$getIdsHolder();

	/**
	 * @return The next free numerical id in the registry
	 */
	default int fabric$nextId() {
		return fabric$getIdsHolder().fabric$nextId();
	}

	/**
	 * Set this registry-like's {@link IdsHolder} instance.
	 * Mainly used during registry remapping.
	 * @param ids The updated ids holder instance
	 */
	void fabric$updateRegistry(IdsHolder<T> ids);
}
