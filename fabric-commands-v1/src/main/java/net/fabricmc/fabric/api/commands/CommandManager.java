package net.fabricmc.fabric.api.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public interface CommandManager {

	static LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(final String name, final ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
