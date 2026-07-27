package net.legacyfabric.fabric.impl.registry.accessor;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;

public interface RegistryWithEvents<T> {
	/**
	 * Get the {@link Event} for the {@link RegistryEntryAddedCallback} for this registry.
	 *
	 * @return the event
	 */
	Event<RegistryEntryAddedCallback<T>> fabric$getEntryAddedCallback();

	/**
	 * Get the {@link Event} for the {@link RegistryBeforeAddCallback} for this registry.
	 *
	 * @return the event
	 */
	Event<RegistryBeforeAddCallback<T>> fabric$getBeforeAddedCallback();
}
