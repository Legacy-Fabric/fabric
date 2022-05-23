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

package net.legacyfabric.fabric.api.client.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FabricClientCommandSource extends CommandSource {
	/**
	 * Sends a feedback message to the player.
	 *
	 * @param message the feedback message
	 */
	void sendFeedback(Text message);

	/**
	 * Sends an error message to the player.
	 *
	 * @param message the error message
	 */
	void sendError(Text message);

	/**
	 * Gets the client instance used to run the command.
	 *
	 * @return the client
	 */
	MinecraftClient getClient();

	/**
	 * Gets the world where the player used the command.
	 *
	 * @return the world
	 */
	ClientWorld getWorld();
}
