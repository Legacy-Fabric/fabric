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

package net.legacyfabric.fabric.api.client.rendering.v1;

import java.util.function.Function;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.legacyfabric.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;

/**
 * Helper class for registering BlockEntityRenderers.
 */
public interface BlockEntityRendererRegistry {
	BlockEntityRendererRegistry INSTANCE = new BlockEntityRendererRegistryImpl();

	/**
	 * Register a BlockEntityRenderer for a BlockEntityType. Can be called clientside before the world is rendered.
	 *
	 * @param clazz               the block entity {@link Class} to register a renderer for
	 * @param blockEntityRenderer a function that returns a {@link BlockEntityRenderer}, called
	 *                            when {@link BlockEntityRenderDispatcher} is initialized or immediately if the dispatcher
	 *                            class is already loaded
	 * @param <E>                 the {@link BlockEntity}
	 */
	<E extends BlockEntity> void register(Class<E> clazz, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<E>> blockEntityRenderer);
}
