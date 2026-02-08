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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.client.resource.pack.ResourcePack;

import net.legacyfabric.fabric.impl.resource.loader.ModResourcePackUtil;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	private void fabric_modifyResourcePackList(List<ResourcePack> list) {
		List<ResourcePack> oldList = Lists.newArrayList(list);
		list.clear();

		boolean appended = false;

		for (ResourcePack pack : oldList) {
			list.add(pack);

			boolean isDefaultResources = pack instanceof BuiltInResourcePack;

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

	@Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/LanguageManager;reload(Ljava/util/List;)V"))
	public void reloadResources(CallbackInfo ci, @Local(ordinal = 0) ArrayList list) {
		fabric_modifyResourcePackList(list);
	}
}
