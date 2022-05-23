package net.legacyfabric.fabric.api.client.commands;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;

public abstract class FabricAbstractClientCommand extends AbstractCommand implements FabricClientCommand {
	@Override
	public boolean isAccessible(CommandSource source) {
		return source instanceof FabricClientCommandSource;
	}
}
