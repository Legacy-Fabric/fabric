package net.fabricmc.fabric.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.commands.ServerCommandSource;

public class CommandManagerHolder {

	public static CommandDispatcher<ServerCommandSource> COMMAND_DISPATCHER;
}
