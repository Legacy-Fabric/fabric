package net.fabricmc.fabric.impl.commands;

import net.fabricmc.fabric.api.commands.FabricCommandRegistry;
import net.minecraft.command.AbstractCommand;
import net.minecraft.util.Pair;

public class FabricCommandRegistryImpl implements FabricCommandRegistry {
	@Override
	public void register(AbstractCommand command, boolean dedicated) {
		FABRIC_COMMANDS.add(new Pair<>(command,dedicated));
	}
}
