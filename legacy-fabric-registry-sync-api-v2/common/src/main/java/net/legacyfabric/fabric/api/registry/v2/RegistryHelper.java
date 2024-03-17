package net.legacyfabric.fabric.api.registry.v2;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

import java.util.function.Function;

public class RegistryHelper {
	public static <T> void register(RegistryHolder<T> registry, Identifier identifier, T value) {
		RegistryHelperImplementation.register(registry, identifier, value);
	}

	public static <T> void registry(Identifier registryId, Identifier identifier, T value) {
		register(RegistryHelperImplementation.getRegistry(registryId), identifier, value);
	}

	public static <T> T register(RegistryHolder<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		return RegistryHelperImplementation.register(registry, identifier, valueConstructor);
	}

	public static <T> T registry(Identifier registryId, Identifier identifier, Function<Integer, T> valueConstructor) {
		return register(RegistryHelperImplementation.<T>getRegistry(registryId), identifier, valueConstructor);
	}

	public static void addRegistry(Identifier identifier, RegistryHolder<?> registryHolder) {
		RegistryHelperImplementation.registerRegistry(identifier, registryHolder);
	}
}
