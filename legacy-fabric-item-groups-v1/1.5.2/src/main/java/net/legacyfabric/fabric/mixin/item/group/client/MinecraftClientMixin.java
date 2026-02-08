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

package net.legacyfabric.fabric.mixin.item.group.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.item.group.MinecraftAccessor;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin implements MinecraftAccessor {
	@Shadow
	public net.minecraft.client.render.texture.TextureManager textureManager;

	@Override
	public void legacy_fabric_api$bindTexture(Identifier location) {
		textureManager.bind("/assets/" + location.getNamespace() + "/" + location.getPath());
	}
}
