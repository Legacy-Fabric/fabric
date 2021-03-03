/*
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

package net.fabricmc.fabric.impl.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;

import net.fabricmc.fabric.api.client.render.v1.EntityRendererRegistry;

public class EntityRendererRegistryImpl implements EntityRendererRegistry {
	private final Map<EntityRenderDispatcher, Context> renderManagerMap = new WeakHashMap<>();
	private final Map<Class<? extends Entity>, Factory> renderSupplierMap = new HashMap<>();

	public void initialize(EntityRenderDispatcher dispatcher, TextureManager textureManager, ItemRenderer itemRenderer, Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> map) {
		synchronized (renderSupplierMap) {
			if (renderManagerMap.containsKey(dispatcher)) {
				return;
			}

			Context context = new Context(textureManager, itemRenderer, map);
			renderManagerMap.put(dispatcher, context);

			for (Class<? extends Entity> c : renderSupplierMap.keySet()) {
				map.put(c, renderSupplierMap.get(c).create(dispatcher, context));
			}
		}
	}

	@Override
	public void register(Class<? extends Entity> entityClass, Factory factory) {
		synchronized (renderSupplierMap) {
			renderSupplierMap.put(entityClass, factory);

			for (EntityRenderDispatcher manager : renderManagerMap.keySet()) {
				renderManagerMap.get(manager).rendererMap.put(entityClass, factory.create(manager, renderManagerMap.get(manager)));
			}
		}
	}

	public static final class Context {
		private final TextureManager textureManager;
		private final ItemRenderer itemRenderer;
		private final Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> rendererMap;

		private Context(TextureManager textureManager, ItemRenderer itemRenderer, Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> rendererMap) {
			this.textureManager = textureManager;
			this.itemRenderer = itemRenderer;
			this.rendererMap = rendererMap;
		}

		public TextureManager getTextureManager() {
			return textureManager;
		}

		public ItemRenderer getItemRenderer() {
			return itemRenderer;
		}
	}

	@FunctionalInterface
	public interface Factory {
		EntityRenderer<? extends Entity> create(EntityRenderDispatcher dispatcher, Context context);
	}
}
