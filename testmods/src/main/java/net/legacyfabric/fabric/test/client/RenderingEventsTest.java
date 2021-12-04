/*
 * Copyright (c) 2021 Legacy Rewoven
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
