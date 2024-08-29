package net.legacyfabric.fabric.impl.registry;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.RegistryEntryCreator;
import net.legacyfabric.fabric.api.util.Identifier;

import java.util.function.Function;

public class RegistryEntryCreatorImpl<T> implements RegistryEntryCreator<T> {
	private final Identifier identifier;
	private final int offset;
	private final Function<Integer, T> valueGetter;

	private T value;

	public RegistryEntryCreatorImpl(Identifier identifier, int offset, Function<Integer, T> valueGetter) {
		this.identifier = identifier;
		this.offset = offset;
		this.valueGetter = valueGetter;
	}

	@Override
	public T getValue(int id) {
		if (this.value == null) {
			this.value = this.valueGetter.apply(id);
		}

		return this.value;
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public int getIdOffset() {
		return this.offset;
	}
}
