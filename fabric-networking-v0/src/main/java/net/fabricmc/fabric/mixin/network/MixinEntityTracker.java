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

package net.fabricmc.fabric.mixin.network;

import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.fabricmc.fabric.api.network.server.EntityTrackerStreamAccessor;

@Mixin(EntityTracker.class)
public class MixinEntityTracker implements EntityTrackerStreamAccessor {
	@Shadow
	public Set<ServerPlayerEntity> players;

	@Override
	public Stream<ServerPlayerEntity> fabric_getTrackingPlayers() {
		return this.players.stream();
	}
}
