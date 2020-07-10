/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.event.player;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

/**
 * Callback for right-clicking ("using") an item.
 * Is hooked in before the spectator check, so make sure to check for the player's game mode as well!
 *
 * <p>Upon return:
 * <ul><li>SUCCESS cancels further processing and, on the client, sends a packet to the server.
 * <li>PASS falls back to further processing.
 * <li>FAIL cancels further processing and does not send a packet to the server.</ul>
 */
public interface UseItemCallback {
	Event<UseItemCallback> EVENT = EventFactory.createArrayBacked(UseItemCallback.class,
			listeners -> (player, world) -> {
				for (UseItemCallback event : listeners) {
					TypedActionResult<ItemStack> result = event.interact(player, world);

					if (result.getResult() != ActionResult.PASS) {
						return result;
					}
				}

				return TypedActionResult.pass(new ItemStack(Blocks.AIR));
			}
	);

	TypedActionResult<ItemStack> interact(PlayerEntity player, World world);
}
