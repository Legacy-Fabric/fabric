package net.legacyfabric.fabric.test.client;

import java.awt.Color;

import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback;
import net.legacyfabric.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.resource.language.I18n;

import net.fabricmc.api.ClientModInitializer;

public class RenderingEventsTest implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((client, tickDelta) -> {
			client.textRenderer.draw(I18n.translate("legacyfabric.api.youreTesting"), 10, 10, Color.RED.getRGB());
		});
		InvalidateRenderStateCallback.EVENT.register(() -> {
			LOGGER.info("Render state invalidated");
		});
	}
}
