package net.legacyfabric.fabric.test.client;

import java.awt.Color;

import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.resource.language.I18n;

import net.fabricmc.api.ClientModInitializer;

public class HudRenderingTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((client, tickDelta) -> {
			client.textRenderer.draw(I18n.translate("legacyfabric.api.youreTesting"), 10, 10, Color.RED.getRGB());
		});
	}
}
