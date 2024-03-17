package net.legacyfabric.fabric.api.registry.v2;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.util.Identifier;

public interface RegistryHolder<T> {
	Identifier getId();
	Event<RegistryEntryAddedCallback<T>> getEntryAddedCallback();
	Event<RegistryBeforeAddCallback<T>> getBeforeAddedCallback();

	void register(int rawId, Identifier identifier, T value);

	<K> K toKeyType(Object o);
}
