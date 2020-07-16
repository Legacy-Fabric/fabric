package net.fabricmc.fabric.api.renderer.v1;

public interface RendererAccess {
	void registerRenderer(Renderer plugin);

	Renderer getRenderer();

	boolean hasRenderer();
}
