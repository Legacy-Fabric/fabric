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

package net.legacyfabric.fabric.mixin.resource.loader.client.osl;

import java.util.List;
import java.util.stream.Collectors;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloader;
import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class ReloadableResourceManagerImplMixin {
	@Shadow
	public abstract void registerReloader(NamespacedIdentifier identifier, ResourceReloader reloader);

	@Shadow
	public abstract void registerReloaderDependencies(NamespacedIdentifier identifier, NamespacedIdentifier... dependencies);

	@WrapMethod(method = "addReloader")
	private void lf$handleIdentifierReloaders(ResourceReloader reloader, Operation<Void> original) {
		if (reloader instanceof IdentifiableResourceReloader) {
			this.registerReloader(((IdentifiableResourceReloader) reloader).getFabricId(), reloader);

			List<NamespacedIdentifier> dependencies = ((IdentifiableResourceReloader) reloader).getFabricDependencies().stream().map(ResourceManagerHelperImpl::mapDependencyId).collect(Collectors.toList());

			if (!dependencies.isEmpty()) {
				this.registerReloaderDependencies(((IdentifiableResourceReloader) reloader).getFabricId(), dependencies.toArray(new NamespacedIdentifier[0]));
			}
		} else {
			original.call(reloader);
		}
	}
}
