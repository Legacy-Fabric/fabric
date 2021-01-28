/*
 * Copyright (c) 2016-2021 FabricMC
 * Copyright (c) 2020-2021 Legacy Fabric
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

package net.legacyfabric.fabric.test.gamerule;

import net.legacyfabric.fabric.api.gamerule.v1.GameRuleRegistrationCallback;

import net.minecraft.world.GameRuleManager;

import net.fabricmc.api.ModInitializer;

public class GameRuleTest implements ModInitializer {
	@Override
	public void onInitialize() {
		GameRuleRegistrationCallback.EVENT.register(gameRuleManager -> {
			gameRuleManager.addGameRule("someNumberGameRule", "1", GameRuleManager.ValueType.NUMERICAL_VALUE);
			gameRuleManager.addGameRule("someBooleanGameRule", "false", GameRuleManager.ValueType.BOOLEAN_VALUE);
			gameRuleManager.addGameRule("someStrGameRule", "foo", GameRuleManager.ValueType.ANY_VALUE);
		});
	}
}
