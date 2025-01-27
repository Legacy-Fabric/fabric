package net.legacyfabric.fabric.api.registry;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

/**
 * An event for registering commands to the {@link CommandRegistry}
 */
@FunctionalInterface
public interface CommandRegistrationCallback {
	Event<CommandRegistrationCallback> EVENT = EventFactory.createArrayBacked(CommandRegistrationCallback.class, listeners -> registry -> {
		for (CommandRegistrationCallback registrar : listeners) {
			registrar.register(registry);
		}
	});

	/**
	 * Register your commands here.
	 *
	 * @param registry The command registry
	 */
	void register(CommandRegistry registry);
}
