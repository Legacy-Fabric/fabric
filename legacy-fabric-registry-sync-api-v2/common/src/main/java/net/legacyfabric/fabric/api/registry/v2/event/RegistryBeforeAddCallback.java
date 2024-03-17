package net.legacyfabric.fabric.api.registry.v2.event;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.RegistryHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

@FunctionalInterface
public interface RegistryBeforeAddCallback<T> {
	void onEntryAdding(int rawId, Identifier id, T object);

	static <T> Event<RegistryBeforeAddCallback<T>> event(Identifier registryId) {
		return event(RegistryHelperImplementation.getRegistry(registryId));
	}

	static <T> Event<RegistryBeforeAddCallback<T>> event(RegistryHolder<T> registry) {
		return registry.getBeforeAddedCallback();
	}
}
