package net.fabricmc.fabric.api.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class BlockEntityRendererRegistry {
	public static final BlockEntityRendererRegistry INSTANCE = new BlockEntityRendererRegistry();

	private Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderers = null;
	private Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderersTmp = new HashMap<>();

	private BlockEntityRendererRegistry(){
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
