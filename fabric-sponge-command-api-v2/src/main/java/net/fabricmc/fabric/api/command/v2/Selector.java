package net.fabricmc.fabric.api.command.v2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

public enum Selector {
	ALL_ENTITIES('e'),
	ALL_PLAYERS('a'),
	NEAREST_PLAYER('p'),
	RANDOM_PLAYER('r'),
	EXECUTING_ENTITY('e'),
	;

	private final char key;
	private static final char[] KEYS = new char[values().length];

	Selector(char key) {
		this.key = key;
	}

	public static List<String> complete(String s) {
		if (s.startsWith("@") && s.length() == 2) {
			return Arrays.stream(values()).map(Selector::getKey).map(String::valueOf).distinct().collect(Collectors.toList());
		}
		return ImmutableList.of();
	}

	public char getKey() {
		return this.key;
	}

	p

	public static Selector parse(String value) {
		if (value.startsWith("@") && value.length() == 2) {
			Arrays.stream(values()).map(e -> "@" + e.key).filter(e -> e.equals(value)).findFirst().orElse(null);
		}
	}
}
