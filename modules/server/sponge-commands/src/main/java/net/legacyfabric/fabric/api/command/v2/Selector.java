/*
 * Copyright (c) 2021 Legacy Rewoven
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

package net.legacyfabric.fabric.api.command.v2;

import java.util.Set;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public enum Selector {
	ALL_ENTITIES('e') {
		@Override
		public Set<Entity> resolve(CommandSource sender) {
			return Sets.newHashSet(sender.getWorld().entities);
		}
	},
	ALL_PLAYERS('a') {
		@Override
		public Set<Entity> resolve(CommandSource sender) {
			return (Set<Entity>) sender.getWorld().playerEntities.stream().map(e -> (Entity) e).collect(Collectors.toSet());
		}
	},
	NEAREST_PLAYER('p') {
		@Override
		public Set<Entity> resolve(CommandSource sender) {
			return Sets.newHashSet(sender.getWorld().getClosestPlayer(sender.method_4086().x, sender.method_4086().y, sender.method_4086().z, 50.0D));
		}
	},
	RANDOM_PLAYER('r') {
		@Override
		public Set<Optional> resolve(CommandSource sender) {
			return Sets.newHashSet(MinecraftServer.getServer().getPlayerManager().players.stream().findAny());
		}
	},
	EXECUTING_ENTITY('s') {
		@Override
		public Set<Entity> resolve(CommandSource sender) {
			//TODO @s didn't exist this early in the game's development, and there seems to be no code to handle it, so maybe this'll work?
			return Sets.newHashSet(sender.getWorld().getPlayerByName(sender.getTranslationKey()));
		}
	};

	private final char key;
	private static final Map<String, Selector> MAP;

	Selector(char key) {
		this.key = key;
	}

	public char getKey() {
		return this.key;
	}

	public abstract Set resolve(CommandSource sender);

	public static List<String> complete(String s) {
		if (s.startsWith("@") && s.length() == 2) {
			return Arrays.stream(values()).map(Selector::getKey).map(String::valueOf).distinct().collect(Collectors.toList());
		}

		return ImmutableList.of();
	}

	public static Selector parse(String value) {
		if (MAP.containsKey(value)) {
			return MAP.get(value);
		}

		throw new IllegalArgumentException("Unknown selector");
	}

	static {
		ImmutableMap.Builder<String, Selector> builder = ImmutableMap.builder();

		for (Selector s : values()) {
			builder.put("@" + s.getKey(), s);
		}

		MAP = builder.build();
	}
}
