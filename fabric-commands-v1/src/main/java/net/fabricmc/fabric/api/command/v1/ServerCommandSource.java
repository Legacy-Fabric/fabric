/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.api.command.v1;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.OperatorEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ServerCommandSource {
	private final CommandSource source;
	private final Vec3d position;
	private final ServerWorld world;
	private final Entity entity;
	private final int permissionLevel;

	public ServerCommandSource(CommandSource source, Vec3d position, ServerWorld world, Entity entity, int permissionLevel) {
		this.source = source;
		this.position = position;
		this.world = world;
		this.entity = entity;
		this.permissionLevel = permissionLevel;
	}

	public CommandSource getSource() {
		return this.source;
	}

	public Vec3d getPosition() {
		return this.position;
	}

	public ServerWorld getWorld() {
		return this.world;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public int getPermissionLevel() {
		return this.permissionLevel;
	}

	public boolean isPlayer() {
		return this.entity instanceof ServerPlayerEntity;
	}

	public boolean hasPermissionLevel(int level) {
		return this.permissionLevel <= level;
	}

	public ServerCommandSource withPosition(Vec3d position) {
		return new ServerCommandSource(this.source, position, this.world, this.entity, this.permissionLevel);
	}

	public ServerCommandSource withWorld(ServerWorld world) {
		return new ServerCommandSource(this.source, this.position, world, this.entity, this.permissionLevel);
	}

	public ServerCommandSource withEntity(Entity entity) {
		return new ServerCommandSource(this.source, this.position, this.world, entity, this.permissionLevel);
	}

	public ServerCommandSource withPermission(int permissionLevel) {
		return new ServerCommandSource(this.source, this.position, this.world, this.entity, permissionLevel);
	}

	public void sendFeedback(Text text) {
		if (this.source.sendCommandFeedback()) {
			this.source.sendMessage(text);
		}
	}

	public static ServerCommandSource from(CommandSource source) {
		int permissionLevel = 0;

		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			OperatorEntry operatorEntry = player.server.getPlayerManager().getOpList().get(player.getGameProfile());

			if (operatorEntry != null) {
				permissionLevel = operatorEntry.getPermissionLevel();
			}
		}

		return new ServerCommandSource(source, source.getPos(), (ServerWorld) source.getWorld(), source.getEntity(), permissionLevel);
	}
}
