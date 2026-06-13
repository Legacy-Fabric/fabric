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

package net.legacyfabric.fabric.api.resource;

import java.util.Collection;
import java.util.Collections;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;

public interface IdentifiableResourceReloader extends ResourceReloader {
	/**
	 * @return The unique identifier of this listener.
	 */
	NamespacedIdentifier getFabricId();

	/**
	 * @return The identifiers of listeners this listener expects to have been
	 * executed before itself. Please keep in mind that this only takes effect
	 * during the application stage!
	 */
	default Collection<NamespacedIdentifier> getFabricDependencies() {
		return Collections.emptyList();
	}

	@Override
	default String getName() {
		return getFabricId().toString();
	}
}
