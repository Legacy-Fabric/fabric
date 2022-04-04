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

package net.legacyfabric.fabric.api.gamerule.v1;

import net.minecraft.world.GameRuleManager;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

/**
 * Allows adding custom game rules.
 * <p>To add a game rule, hook into {@code GameRulesInitializedCallback}
 * and call {@code dispatcher.addGameRule("name", "default_value", variableType)}</p>
 */
public interface GameRulesInitializedCallback {
	Event<GameRulesInitializedCallback> EVENT = EventFactory.createArrayBacked(GameRulesInitializedCallback.class,
			(listeners) -> (dispatcher) -> {
				for (GameRulesInitializedCallback event : listeners) {
					event.onGameRulesRegistered(dispatcher);
				}
			}
	);

	void onGameRulesRegistered(GameRuleManager dispatcher);
}
