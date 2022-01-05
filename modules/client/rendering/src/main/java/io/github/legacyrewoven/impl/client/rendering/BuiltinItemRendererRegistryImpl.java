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

package io.github.legacyrewoven.impl.client.rendering;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.legacyrewoven.api.client.rendering.v1.BuiltinItemRendererRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class BuiltinItemRendererRegistryImpl implements BuiltinItemRendererRegistry {
	public static final BuiltinItemRendererRegistryImpl INSTANCE = new BuiltinItemRendererRegistryImpl();

	private static final Map<Item, DynamicItemRenderer> RENDERERS = new HashMap<>();

	private BuiltinItemRendererRegistryImpl() {
	}

	@Override
	public void register(@NotNull Item item, @NotNull DynamicItemRenderer renderer) {
		Objects.requireNonNull(item, "Item is null");
		Objects.requireNonNull(renderer, "Renderer is null");

		if (RENDERERS.putIfAbsent(item, renderer) != null) {
			throw new IllegalArgumentException("Item " + Item.REGISTRY.method_7332(item) + " already has a builtin renderer!");
		}
	}

	@Nullable
	public static DynamicItemRenderer getRenderer(Item item) {
		return RENDERERS.get(item);
	}
}
