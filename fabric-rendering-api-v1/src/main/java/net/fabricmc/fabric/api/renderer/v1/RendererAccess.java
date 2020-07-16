package net.fabricmc.fabric.api.renderer.v1;

import net.fabricmc.fabric.impl.renderer.RendererAccessImpl;

public interface RendererAccess {
	RendererAccess INSTANCE = RendererAccessImpl.INSTANCE;

	void registerRenderer(Renderer plugin);

	Renderer getRenderer();

	boolean hasRenderer();
}
