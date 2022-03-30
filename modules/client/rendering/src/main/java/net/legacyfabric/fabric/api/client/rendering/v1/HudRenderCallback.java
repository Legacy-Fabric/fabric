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

package net.legacyfabric.fabric.api.client.rendering.v1;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;

public interface HudRenderCallback {
	Event<HudRenderCallback> EVENT = EventFactory.createArrayBacked(HudRenderCallback.class, (listeners) -> (client, delta) -> {
		for (HudRenderCallback event : listeners) {
			event.onHudRender(client, delta);
		}
	});

	/**
	 * Called after rendering the whole hud, which is displayed in game, in a world.
	 *
	 * @param client    the client
	 * @param tickDelta Progress for linearly interpolating between the previous and current game state
	 */
	void onHudRender(MinecraftClient client, float tickDelta);
}
