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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceReloaderIdentifier;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.manager.ResourceReloadListener;

import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;

@Mixin(ResourceManagerAdapter.class)
public class ResourceManagerAdapterMixin {
	@Shadow
	@Final
	private SimpleReloadableResourceManager resourceManager;

	@WrapOperation(method = "addListener", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/resource/loader/impl/adapter/ResourceReloaderIdentifier;identify(Lnet/minecraft/client/resource/manager/ResourceReloadListener;)Lnet/ornithemc/osl/core/api/util/NamespacedIdentifier;"))
	private NamespacedIdentifier lf$getListenerIdentifier(ResourceReloaderIdentifier instance, ResourceReloadListener resourceReloadListener, Operation<NamespacedIdentifier> original) {
		if (resourceReloadListener instanceof IdentifiableResourceReloadListener) {
			return ((IdentifiableResourceReloadListener) resourceReloadListener).getFabricId();
		}

		return original.call(instance, resourceReloadListener);
	}

	@Inject(method = "addListener", at = @At("RETURN"))
	private void lf$registerDependencies(ResourceReloadListener listener, CallbackInfo ci,
										@Local NamespacedIdentifier identifier) {
		if (listener instanceof IdentifiableResourceReloadListener && identifier != null) {
			List<NamespacedIdentifier> dependencies = ((IdentifiableResourceReloadListener) listener).getFabricDependencies().stream().map(ResourceManagerHelperImpl::mapDependencyId).collect(Collectors.toList());
			if (!dependencies.isEmpty()) this.resourceManager.registerReloaderDependencies(identifier, dependencies.toArray(new NamespacedIdentifier[0]));
		}
	}
}
