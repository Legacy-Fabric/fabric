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

package net.fabricmc.fabric.api.event.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;

/**
 * Callback for breaking a block.
 *
 * <p>Upon return:
 * <ul><li>SUCCESS cancels further processing and breaks the block.
 * <li>PASS falls back to further processing.
 * <li>FAIL cancels further processing and does not break the block.</ul>
 */
public interface BreakBlockCallback {
	Event<BreakBlockCallback> EVENT = EventFactory.createArrayBacked(BreakBlockCallback.class,
			(listeners) -> (player, world, pos) -> {
				for (BreakBlockCallback event : listeners) {
					ActionResult result = event.blockBreak(player, world, pos);

					if (result != ActionResult.PASS) {
						return result;
					}
				}

				return ActionResult.PASS;
			}
	);

	ActionResult blockBreak(PlayerEntity player, World world, BlockPos pos);
}
