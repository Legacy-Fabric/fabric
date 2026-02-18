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

package net.legacyfabric.fabric.mixin.base;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.legacyfabric.fabric.api.util.OSLIdentifierExtension;

@Mixin(NamespacedIdentifier.class)
public interface NamespacedIdentifierMixin extends OSLIdentifierExtension {
	@Shadow
	String namespace();

	@Shadow
	String identifier();

	@Override
	default String asTranslationKey() {
		return this.namespace() + "." + this.identifier();
	}
}
