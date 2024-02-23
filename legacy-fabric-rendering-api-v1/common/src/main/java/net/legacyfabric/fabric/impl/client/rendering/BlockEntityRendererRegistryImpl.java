/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.impl.client.rendering;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.legacyfabric.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class BlockEntityRendererRegistryImpl implements BlockEntityRendererRegistry {
	private static final HashMap<Class<? extends BlockEntity>, Function<BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<?>>> map = new HashMap<>();
	private static BiConsumer<Class<? extends BlockEntity>, Function<BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<?>>> handler = (type, function) -> map.put(type, function);

	@Override
	public <E extends BlockEntity> void register(Class<E> clazz, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<E>> blockEntityRenderer) {
		handler.accept(clazz, blockEntityRenderer);
	}

	public static void setup(BiConsumer<Class<? extends BlockEntity>, Function<BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<?>>> vanillaHandler) {
		map.forEach(vanillaHandler);
		handler = vanillaHandler;
	}
}
