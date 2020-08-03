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

	public void initialize(EntityRenderDispatcher manager, TextureManager textureManager, ItemRenderer itemRenderer, Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> map) {
		synchronized (renderSupplierMap) {
			if (renderManagerMap.containsKey(manager)) {
				return;
			}

			Context context = new Context(textureManager, itemRenderer, map);
			renderManagerMap.put(manager, context);

			for (Class<? extends Entity> c : renderSupplierMap.keySet()) {
				map.put(c, renderSupplierMap.get(c).create(manager, context));
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
		EntityRenderer<? extends Entity> create(EntityRenderDispatcher manager, Context context);
	}
}
