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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.pack.AbstractTexturePack;
import net.minecraft.client.resource.pack.TexturePack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.resource.ModTexturePack;
import net.legacyfabric.fabric.impl.resource.loader.TexturePackHelper;

@Environment(EnvType.CLIENT)
@Mixin(AbstractTexturePack.class)
public class AbstractTexturePackMixin {
	@Unique
	private List<ModTexturePack> fabric_texturePacks;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;loadIcon()V"))
	private void registerPacks(String file, File string2, String iTexturePack, TexturePack par4, CallbackInfo ci) {
		if (this instanceof ModTexturePack) return;

		fabric_texturePacks = TexturePackHelper.getTexturePacks();
	}

	@Inject(method = "getResource(Ljava/lang/String;Z)Ljava/io/InputStream;", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;defaultTextures:Lnet/minecraft/client/resource/pack/TexturePack;", ordinal = 0), cancellable = true)
	private void lf$getModResource(String bl, boolean par2, CallbackInfoReturnable<InputStream> cir) {
		if (par2) {
			if ("/pack.txt".equals(bl) || "/pack.png".equals(bl)) return;

			for (ModTexturePack pack : fabric_texturePacks) {
				try {
					InputStream stream = pack.getResource(bl);

					if (stream != null) {
						cir.setReturnValue(stream);
						return;
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
	}

	@Inject(method = "hasResource(Ljava/lang/String;Z)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;defaultTextures:Lnet/minecraft/client/resource/pack/TexturePack;", ordinal = 0), cancellable = true)
	private void lf$hasModResource(String bl, boolean par2, CallbackInfoReturnable<Boolean> cir) {
		if (par2) {
			if ("/pack.txt".equals(bl) || "/pack.png".equals(bl)) return;

			for (ModTexturePack pack : fabric_texturePacks) {
				boolean exists = pack.hasResource(bl, false);

				if (exists) {
					cir.setReturnValue(true);
					return;
				}
			}
		}
	}
}
