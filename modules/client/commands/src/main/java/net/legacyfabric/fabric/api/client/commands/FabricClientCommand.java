package net.legacyfabric.fabric.api.client.commands;

import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;

public interface FabricClientCommand extends Command {
	@Override
	default void execute(CommandSource source, String[] args) {
		if (source instanceof FabricClientCommandSource) {
			this.execute((FabricClientCommandSource) source, args);
		} else {
			throw new IllegalArgumentException("Command source is not instance of FabricClientCommandSource!");
		}
	}

	void execute(FabricClientCommandSource source, String[] args);
}
