package net.fabricmc.fabric.api.command.v2;

import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandManager;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * An entrypoint and event for registering commands to the {@link CommandManager}
 */
@FunctionalInterface
public interface CommandRegistrar {
	Event<CommandRegistrar> EVENT = EventFactory.createArrayBacked(CommandRegistrar.class, listeners -> (manager, dedicated) -> {
		for (CommandRegistrar registrar : listeners) {
			registrar.register(manager, dedicated);
		}
	});

	/**
	 * Register your commands here
	 * @param manager The command manager
	 * @param dedicated Whether the mod is running on a dedicated server
	 */
	void register(CommandManager manager, boolean dedicated);
}
