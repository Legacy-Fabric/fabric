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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
@Mixin(DefaultResourcePack.class)
public class DefaultResourcePackMixin {
	@Inject(method = "openClassLoader", at = @At("HEAD"), cancellable = true)
	protected void onFindInputStream(Identifier identifier, CallbackInfoReturnable<InputStream> callback) {
		//		if (DefaultResourcePack.resourcePath != null) {
		// Fall through to Vanilla logic, they have a special case here.
		//			return;
		//		}

		String path = "assets/" + identifier.getNamespace() + "/" + identifier.getPath();
		URL found = null;

		try {
			Enumeration<URL> candidates = DefaultResourcePack.class.getClassLoader().getResources(path);

			// Get the last element
			while (candidates.hasMoreElements()) {
				found = candidates.nextElement();
			}

			if (found == null) {
				// Mimics vanilla behavior

				callback.setReturnValue(null);
				return;
			}
		} catch (IOException var6) {
			// Default path
		}

		try {
			if (found != null) {
				callback.setReturnValue(found.openStream());
			}
		} catch (Exception e) {
			// Default path
		}
	}

	@WrapOperation(method = "parseMetadata", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"))
	private InputStream fixPackMetaPath(Class instance, String e, Operation<InputStream> original) throws IOException {
		Path path = FabricLoader.getInstance().getModContainer("minecraft").get().findPath(e).orElse(null);

		if (path == null) return null;

		return Files.newInputStream(path);
	}
}
