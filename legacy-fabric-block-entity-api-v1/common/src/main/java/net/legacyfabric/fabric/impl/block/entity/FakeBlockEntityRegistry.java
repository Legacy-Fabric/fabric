package net.legacyfabric.fabric.impl.block.entity;

import net.minecraft.block.entity.BlockEntity;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.WritableRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;

public class FakeBlockEntityRegistry implements WritableRegistry<Class<? extends BlockEntity>> {
	private final BiFunction<NamespacedIdentifier, Class<? extends BlockEntity>, Class<? extends BlockEntity>> registryFunction;

	public FakeBlockEntityRegistry(BiFunction<NamespacedIdentifier, Class<? extends BlockEntity>, Class<? extends BlockEntity>> registryFunction) {
		this.registryFunction = registryFunction;
	}

	@Override
	public <V extends Class<? extends BlockEntity>> V register(ResourceKey<Class<? extends BlockEntity>> key, V value) {
		return (V) this.registryFunction.apply(key.identifier(), value);
	}

	@Override
	public <V extends Class<? extends BlockEntity>> V register(int id, ResourceKey<Class<? extends BlockEntity>> key, V value) {
		return null;
	}

	@Override
	public NamespacedIdentifier identifier() {
		return new Identifier("block_entity_type");
	}

	@Override
	public Class<? extends BlockEntity> get(int id) {
		return null;
	}

	@Override
	public Class<? extends BlockEntity> get(ResourceKey<Class<? extends BlockEntity>> key) {
		return null;
	}

	@Override
	public Class<? extends BlockEntity> get(NamespacedIdentifier identifier) {
		return null;
	}

	@Override
	public boolean has(Class<? extends BlockEntity> value) {
		return false;
	}

	@Override
	public int getId(Class<? extends BlockEntity> value) {
		return 0;
	}

	@Override
	public ResourceKey<Class<? extends BlockEntity>> getKey(Class<? extends BlockEntity> value) {
		return null;
	}

	@Override
	public NamespacedIdentifier getIdentifier(Class<? extends BlockEntity> value) {
		return null;
	}

	@Override
	public Set<ResourceKey<Class<? extends BlockEntity>>> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Set<NamespacedIdentifier> identifierSet() {
		return Collections.emptySet();
	}

	@Override
	public Registry<Class<? extends BlockEntity>> freeze() {
		return null;
	}

	@Override
	public @NotNull Iterator<Class<? extends BlockEntity>> iterator() {
		return null;
	}
}
