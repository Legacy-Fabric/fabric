package net.legacyfabric.fabric.api.registry.v2.registry.registrable;

import net.legacyfabric.fabric.api.util.Identifier;

public interface RegistryEntryCreator<T> {
	T getValue(int id);
	Identifier getIdentifier();
	int getIdOffset();
}
