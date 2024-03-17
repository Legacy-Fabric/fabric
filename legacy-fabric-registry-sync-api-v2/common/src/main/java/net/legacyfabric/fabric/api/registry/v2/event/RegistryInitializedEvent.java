package net.legacyfabric.fabric.api.registry.v2.event;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.RegistryHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

/**
 * Triggered only when a new registry is registered.
 */
@FunctionalInterface
public interface RegistryInitializedEvent {
	<T> void initialized(RegistryHolder<T> registry);

	static Event<RegistryInitializedEvent> event(Identifier registryId) {
		return RegistryHelperImplementation.getInitializationEvent(registryId);
	}
}
