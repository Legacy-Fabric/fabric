/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.mixin.resource.loader.client;

import java.util.List;

import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePack;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Shadow
	private ReloadableResourceManager resourceManager;

	private void fabric_modifyResourcePackList(List<ResourcePack> list) {
		List<ResourcePack> oldList = Lists.newArrayList(list);
		list.clear();

		boolean appended = false;

		for (ResourcePack pack : oldList) {
			list.add(pack);

			boolean isDefaultResources = pack instanceof DefaultResourcePack;

			if (isDefaultResources) {
				ModResourcePackUtil.appendModResourcePacks(list);
				appended = true;
			}
		}

		if (!appended) {
			StringBuilder builder = new StringBuilder("Fabric could not find resource pack injection location!");

			for (ResourcePack rp : oldList) {
				builder.append("\n - ").append(rp.getName()).append(" (").append(rp.getClass().getName()).append(")");
			}

			throw new RuntimeException(builder.toString());
		}
	}

	//	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;"))
	//	public Object initResources(Stream<ResourcePack> stream, Collector collector) {
	//		List<ResourcePack> fabricResourcePacks = stream.collect(Collectors.toList());
	//		fabric_modifyResourcePackList(fabricResourcePacks);
	//		noinspection unchecked
	//		return fabricResourcePacks.stream().collect(collector);
	//	}

	@Inject(method = "stitchTextures",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;method_4357(Ljava/util/List;)V", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void reloadResources(CallbackInfo ci, List<ResourcePack> list) {
		fabric_modifyResourcePackList(list);
	}
}
