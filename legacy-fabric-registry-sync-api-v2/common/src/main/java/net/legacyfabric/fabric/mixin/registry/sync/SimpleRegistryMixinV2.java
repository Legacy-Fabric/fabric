package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.RegistryHolder;

import net.legacyfabric.fabric.api.util.Identifier;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;
import net.legacyfabric.fabric.impl.registry.accessor.RegistryIdSetter;

import net.minecraft.util.registry.SimpleRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixinV2<K, V> implements RegistryHolder<V>, RegistryIdSetter {
	@Unique
	private final Event<RegistryEntryAddedCallback<V>> fabric_addObjectEvent = EventFactory.createArrayBacked(RegistryEntryAddedCallback.class,
			(callbacks) -> (rawId, id, object) -> {
				for (RegistryEntryAddedCallback<V> callback : callbacks) {
					callback.onEntryAdded(rawId, id, object);
				}
			}
	);

	@Unique
	private final Event<RegistryBeforeAddCallback<V>> fabric_beforeAddObjectEvent = EventFactory.createArrayBacked(RegistryBeforeAddCallback.class,
			(callbacks) -> (rawId, id, object) -> {
				for (RegistryBeforeAddCallback<V> callback : callbacks) {
					callback.onEntryAdding(rawId, id, object);
				}
			}
	);

	@Unique
	private Identifier fabric_id;

	@Override
	public Event<RegistryEntryAddedCallback<V>> getEntryAddedCallback() {
		return this.fabric_addObjectEvent;
	}

	@Override
	public Event<RegistryBeforeAddCallback<V>> getBeforeAddedCallback() {
		return this.fabric_beforeAddObjectEvent;
	}

	@Override
	public Identifier getId() {
		return this.fabric_id;
	}

	@Override
	public void fabric$setId(Identifier identifier) {
		assert this.fabric_id == null;
		this.fabric_id = identifier;
	}

	@Override
	public K toKeyType(Object o) {
		return RegistryHelperImplementation.hasFlatteningBegun ? (K) new net.minecraft.util.Identifier(o.toString()) : (K) o.toString();
	}
}
