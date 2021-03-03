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

package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ClientItemEvents {
	/**
	 * Called after the item tooltip is built.
	 * Usage:-
	 * <br><code>
	 *     ClientItemEvents.TOOLTIP.register((stack, player, lines) -> {
	 *         lines.add(I18n.translate("translation.key"));
	 *     });
	 * </code>
	 */
	public static final Event<Tooltip> TOOLTIP = EventFactory.createArrayBacked(Tooltip.class, (listeners) -> (stack, player, lines) -> {
		for (Tooltip callback : listeners) {
			callback.appendTooltip(stack, player, lines);
		}
	});

	public interface Tooltip {
		void appendTooltip(ItemStack stack, PlayerEntity player, List<String> lines);
	}
}
