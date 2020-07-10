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

package net.fabricmc.fabric.api.event.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.util.LoginMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface PlayerJoinCallback {

	/**
	 * this is currently the best way to be able to cancel the login message. any suggestions are open.
	 */
	Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class, (listeners) -> (message, player) -> {
		List<Text> joinMessages = new ArrayList<>();
		for (PlayerJoinCallback callback : listeners) {
			joinMessages.add(callback.playerJoin(message,player));
		}
		if(joinMessages.size() > 0){
			return joinMessages.get(0);
		}
		return message;
	});

	Text playerJoin(Text message, ServerPlayerEntity player);

}
