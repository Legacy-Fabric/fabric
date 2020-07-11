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

package net.fabricmc.fabric.api.server;

import java.util.stream.Stream;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public final class PlayerStream {
	private PlayerStream() {
	}

	public static Stream<ServerPlayerEntity> all(MinecraftServer server) {
		if (server.getPlayerManager() != null) {
			return server.getPlayerManager().getPlayers().stream();
		} else {
			return Stream.empty();
		}
	}

	public static Stream<PlayerEntity> world(World world) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Only supported on ServerWorld!");
		}

		return ((ServerWorld) world).playerEntities.stream();
	}
}
