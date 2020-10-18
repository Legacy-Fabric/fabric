package net.fabricmc.fabric.api.command.v2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public enum Selector {
	ALL_ENTITIES('e'),
	ALL_PLAYERS('a'),
	NEAREST_PLAYER('p'),
	RANDOM_PLAYER('r'),
	EXECUTING_ENTITY('e'),
	;

	private final char key;
	private static final Map<String, Selector> MAP;

	Selector(char key) {
		this.key = key;
	}

	public char getKey() {
		return this.key;
	}

	// TODO: don't hardcode this
	public Set<Entity> resolve(CommandSource sender) {
		if (this == ALL_ENTITIES) {
			return Sets.newHashSet(sender.getWorld().entities);
		} else if (this == ALL_PLAYERS) {
			return sender.getWorld().playerEntities.stream().map(e -> (Entity) e).collect(Collectors.toSet());
		} else if (this == NEAREST_PLAYER) {
			return Sets.newHashSet(sender.getWorld().getClosestPlayer(sender.getPos().x, sender.getPos().y, sender.getPos().z, 50.0D));
		} else if (this == RANDOM_PLAYER) {
			return Sets.newHashSet(MinecraftServer.getServer().getPlayerManager().getPlayers().stream().findAny().orElseThrow(NullPointerException::new));
		}
		return Sets.newHashSet(sender.getEntity());
	}

	public static List<String> complete(String s) {
		if (s.startsWith("@") && s.length() == 2) {
			return Arrays.stream(values()).map(Selector::getKey).map(String::valueOf).distinct().collect(Collectors.toList());
		}
		return ImmutableList.of();
	}

	public static Selector parse(String value) {
		return MAP.get(value);
	}

	static {
		ImmutableMap.Builder<String, Selector> builder = ImmutableMap.builder();
		for (Selector s : values()) {
			builder.put("@" + s.getKey(), s);
		}
		MAP = builder.build();
	}
}
