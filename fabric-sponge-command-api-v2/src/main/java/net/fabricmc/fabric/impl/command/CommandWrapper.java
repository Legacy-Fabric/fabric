package net.fabricmc.fabric.impl.command;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;

import net.fabricmc.fabric.api.command.v2.lib.sponge.spec.CommandSpec;

public class CommandWrapper extends AbstractCommand {
	public CommandWrapper(CommandSpec callable) {

	}

	@Override
	public String getCommandName() {
		return null;
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return null;
	}

	@Override
	public void execute(CommandSource source, String[] args) throws CommandException {

	}
}
