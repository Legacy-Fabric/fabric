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

package net.fabricmc.fabric.api.event.world;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;

@Deprecated
public interface ServerPlayerTickCallback {
	/**
	 * @deprecated Please use {@link ServerPlayerEvents#END_TICK}
	 */
	@Deprecated
	Event<ServerPlayerTickCallback> EVENT = EventFactory.createArrayBacked(ServerPlayerTickCallback.class,
			(listeners) -> (player) -> {
				for (ServerPlayerTickCallback event : listeners) {
					event.tick(player);
				}
			}
	);

	void tick(PlayerEntity player);
}
