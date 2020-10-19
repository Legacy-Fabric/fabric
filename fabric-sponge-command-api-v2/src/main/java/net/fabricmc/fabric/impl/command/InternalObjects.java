package net.fabricmc.fabric.impl.command;

import net.fabricmc.fabric.api.command.v2.lib.sponge.dispatcher.SimpleDispatcher;

public class InternalObjects {
	private static final CommandManagerImpl COMMAND_MANAGER = new CommandManagerImpl(SimpleDispatcher.FIRST_DISAMBIGUATOR);

	public static CommandManagerImpl getCommandManager() {
		return COMMAND_MANAGER;
	}
}
