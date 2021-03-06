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

package net.fabricmc.fabric.api.event.client;

import net.minecraft.client.options.GameOptions;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Deprecated
public interface GameOptionsSavedCallback {
	/**
	 * @deprecated Please use {@link ClientLifecycleEvents#OPTIONS_SAVED}.
	 */
	@Deprecated
	Event<GameOptionsSavedCallback> EVENT = EventFactory.createArrayBacked(GameOptionsSavedCallback.class,
			(listeners) -> (options) -> {
				for (GameOptionsSavedCallback event : listeners) {
					event.onGameOptionsSaved(options);
				}
			}
	);

	void onGameOptionsSaved(GameOptions options);
}
