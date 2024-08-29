package net.legacyfabric.fabric.impl.registry;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.util.Identifier;

class RegistryEntryImpl<T> implements RegistryEntry<T> {
	private final int id;
	private final Identifier identifier;
	private final T value;

	RegistryEntryImpl(int id, Identifier identifier, T value) {
		this.id = id;
		this.identifier = identifier;
		this.value = value;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public T getValue() {
		return this.value;
	}
}
