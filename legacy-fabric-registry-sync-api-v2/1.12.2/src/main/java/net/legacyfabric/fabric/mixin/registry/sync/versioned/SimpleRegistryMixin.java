package net.legacyfabric.fabric.mixin.registry.sync.versioned;

import net.legacyfabric.fabric.api.registry.v2.RegistryHolder;

import net.legacyfabric.fabric.api.util.Identifier;

import net.minecraft.util.registry.SimpleRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<K, V> implements RegistryHolder<V> {
	@Shadow
	public abstract void add(int id, K identifier, V object);

	@Override
	public void register(int rawId, Identifier identifier, V value) {
		getBeforeAddedCallback().invoker().onEntryAdding(rawId, identifier, value);
		add(rawId, toKeyType(identifier), value);
		getEntryAddedCallback().invoker().onEntryAdded(rawId, identifier, value);
	}
}
