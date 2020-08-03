package net.fabricmc.fabric.impl.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.fabricmc.fabric.api.client.render.v1.BlockEntityRendererRegistry;

public class BlockEntityRendererRegistryImpl implements BlockEntityRendererRegistry {
	private Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderers = null;
	private Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderersTmp = new HashMap<>();

	public void register(Class<? extends BlockEntity> blockEntityClass, BlockEntityRenderer<? extends BlockEntity> blockEntityRenderer) {
		if (renderers != null) {
			renderers.put(blockEntityClass, blockEntityRenderer);
			blockEntityRenderer.method_3751(BlockEntityRenderDispatcher.INSTANCE);
		} else {
			renderersTmp.put(blockEntityClass, blockEntityRenderer);
		}
	}

	public void initialize(BlockEntityRenderDispatcher instance, Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> map) {
		if (renderers != null && renderers != map) {
			throw new RuntimeException("Tried to set renderers twice!");
		}

		if (renderers == map) {
			return;
		}

		renderers = map;

		for (BlockEntityRenderer<? extends BlockEntity> renderer : renderersTmp.values()) {
			renderer.method_3751(instance);
		}

		renderers.putAll(renderersTmp);
		renderersTmp = null;
	}
}
