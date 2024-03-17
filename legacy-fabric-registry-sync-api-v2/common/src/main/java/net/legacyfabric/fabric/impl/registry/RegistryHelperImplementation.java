package net.legacyfabric.fabric.impl.registry;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.RegistryHolder;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.util.Identifier;

import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.registry.accessor.RegistryIdSetter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RegistryHelperImplementation {
	public static final boolean hasFlatteningBegun = VersionUtils.matches(">=1.8 <=1.12.2");
	public static final Map<Identifier, Event<RegistryInitializedEvent>> INITIALIZATION_EVENTS = new HashMap<>();
	private static final Map<Identifier, RegistryHolder<?>> REGISTRIES = new HashMap<>();

	public static Event<RegistryInitializedEvent> getInitializationEvent(Identifier registryId) {
		Event<RegistryInitializedEvent> event;

		if (INITIALIZATION_EVENTS.containsKey(registryId)) {
			event = INITIALIZATION_EVENTS.get(registryId);
		} else {
			event = EventFactory.createArrayBacked(RegistryInitializedEvent.class,
					(callbacks) -> new RegistryInitializedEvent() {
						@Override
						public <T> void initialized(RegistryHolder<T> registry) {
							for (RegistryInitializedEvent callback : callbacks) {
								callback.initialized(registry);
							}
						}
					}
			);
			INITIALIZATION_EVENTS.put(registryId, event);
		}

		return event;
	}

	public static <T> RegistryHolder<T> getRegistry(Identifier identifier) {
		return (RegistryHolder<T>) REGISTRIES.get(identifier);
	}

	public static void registerRegistry(Identifier identifier, RegistryHolder<?> holder) {
		if (REGISTRIES.containsKey(identifier)) throw new IllegalArgumentException("Attempted to register registry " + identifier.toString() + " twices!");
		REGISTRIES.put(identifier, holder);

		if (holder instanceof RegistryIdSetter) ((RegistryIdSetter) holder).fabric$setId(identifier);
	}

	public static <T> void register(RegistryHolder<T> registry, Identifier identifier, T value) {
		int computedId = -1;
		registry.register(computedId, identifier, value);
	}

	public static <T> T register(RegistryHolder<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		int computedId = -1;
		T value = valueConstructor.apply(computedId);
		registry.register(computedId, identifier, value);
		return value;
	}
}
