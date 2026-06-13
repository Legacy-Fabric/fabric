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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;

@Mixin(SimpleReloadableResourceManager.class)
public class ReloadableResourceManagerImplMixin {
	@Shadow
	@Final
	private List<ResourceReloader> registeredReloaders;

	@Definition(id = "clear", method = "Lnet/minecraft/client/resource/manager/SimpleReloadableResourceManager;clear()V")
	@Definition(id = "registeredReloaders", field = "Lnet/ornithemc/osl/resource/loader/impl/resource/manager/SimpleReloadableResourceManager;registeredReloaders:Ljava/util/List;")
	@Expression("this.registeredReloaders")
	@Inject(method = "startReload(Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/ornithemc/osl/resource/loader/api/resource/reload/ResourceReload;", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	public void onReload(List<net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack> packs, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask, CallbackInfoReturnable<ResourceReload> cir) {
		ResourceManagerHelperImpl.getInstance().sort(this.registeredReloaders);
	}
}
