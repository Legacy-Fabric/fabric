package net.fabricmc.fabric.api.commands;

import net.fabricmc.fabric.impl.commands.FabricCommandRegistryImpl;
import net.minecraft.command.AbstractCommand;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface FabricCommandRegistry {
	FabricCommandRegistry INSTANCE = new FabricCommandRegistryImpl();
	List<Pair<AbstractCommand,Boolean>> FABRIC_COMMANDS = new ArrayList<>();

	void register(AbstractCommand command, boolean dedicated);

	default void register(AbstractCommand command){
		this.register(command,false);
	}
}
