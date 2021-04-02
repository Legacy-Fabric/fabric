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

package net.legacyfabric.fabric.api.client.rendering.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.ReloadableResourceManager;

/**
 * Helper class for registering EntityRenderers.
 */
public class EntityRendererRegistry {
	@FunctionalInterface
	public interface Factory {
		EntityRenderer<? extends Entity> create(EntityRenderDispatcher manager, Context context);
	}

	public static final class Context {
		private final TextureManager textureManager;
		private final ReloadableResourceManager resourceManager;
		private final ItemRenderer itemRenderer;
		private final Map<Class<? extends Entity>, EntityRenderer<?>> rendererMap;

		private Context(TextureManager textureManager, ReloadableResourceManager resourceManager, ItemRenderer itemRenderer, Map<Class<? extends Entity>, EntityRenderer<?>> rendererMap) {
			this.textureManager = textureManager;
			this.resourceManager = resourceManager;
			this.itemRenderer = itemRenderer;
			this.rendererMap = rendererMap;
		}

		public TextureManager getTextureManager() {
			return textureManager;
		}

		public ReloadableResourceManager getResourceManager() {
			return resourceManager;
		}

		public ItemRenderer getItemRenderer() {
			return itemRenderer;
		}
	}

	public static final EntityRendererRegistry INSTANCE = new EntityRendererRegistry();
	private final Map<EntityRenderDispatcher, Context> renderManagerMap = new WeakHashMap<>();
	private final Map<Class<? extends Entity>, Factory> renderSupplierMap = new HashMap<>();

	private EntityRendererRegistry() { }

	public void initialize(EntityRenderDispatcher manager, TextureManager textureManager, ReloadableResourceManager resourceManager, ItemRenderer itemRenderer, Map<Class<? extends Entity>, EntityRenderer<?>> renderers) {
		synchronized (renderSupplierMap) {
			if (renderManagerMap.containsKey(manager)) {
				return;
			}

			Context context = new Context(textureManager, resourceManager, itemRenderer, renderers);
			renderManagerMap.put(manager, context);

			for (Class<? extends Entity> c : renderSupplierMap.keySet()) {
				renderers.put(c, renderSupplierMap.get(c).create(manager, context));
			}
		}
	}

	public void register(Class<? extends Entity> entityClass, Factory factory) {
		synchronized (renderSupplierMap) {
			// TODO: warn on duplicate
			renderSupplierMap.put(entityClass, factory);

			for (EntityRenderDispatcher manager : renderManagerMap.keySet()) {
				renderManagerMap.get(manager).rendererMap.put(entityClass, factory.create(manager, renderManagerMap.get(manager)));
			}
		}
	}
}
