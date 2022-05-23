package net.legacyfabric.fabric.impl.client.commands;

import net.legacyfabric.fabric.api.client.commands.FabricAbstractClientCommand;
import net.legacyfabric.fabric.api.client.commands.FabricClientCommandManager;
import net.legacyfabric.fabric.api.client.commands.FabricClientCommandSource;
import net.minecraft.command.CommandSource;

public class ClientSetPrefixCommand extends FabricAbstractClientCommand {
	@Override
	public void execute(FabricClientCommandSource source, String[] args) {
		FabricClientCommandManager.INSTANCE.PREFIX = args[0];
	}

	@Override
	public String getCommandName() {
		return "prefix";
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return "legacyfabric.api.prefixcommand";
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
