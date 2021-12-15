/*
 * Copyright (c) 2021 Legacy Rewoven
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package io.github.legacyrewoven.mixin.resource.loader.client;

import java.util.List;

import io.github.legacyrewoven.impl.resource.loader.ResourceManagerHelperImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ReloadableResourceManagerInstance;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReloadListener;

@Mixin(ReloadableResourceManagerInstance.class)
public class ReloadableResourceManagerImplMixin {
	@Shadow
	@Final
	private List<ResourceReloadListener> field_5264;

	@Inject(method = "method_4357", at = @At(value = "INVOKE", remap = false, target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
	public void onReload(List<ResourcePack> resourcePacks, CallbackInfo ci) {
		ResourceManagerHelperImpl.getInstance().sort(this.field_5264);
	}
}
