package net.legacyfabric.fabric.mixin.registry.sync.osl;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;

import net.legacyfabric.fabric.api.util.Identifier;

import net.legacyfabric.fabric.impl.registry.accessor.RegistryWithEvents;

import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.impl.registry.WrappedIdRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WrappedIdRegistry.class)
public class WrappedIdRegistryMixin<T> implements RegistryWithEvents<T> {
	@Unique
	private Event<RegistryEntryAddedCallback<T>> fabric_addObjectEvent;

	@Unique
	private Event<RegistryBeforeAddCallback<T>> fabric_beforeAddObjectEvent;

	@Override
	public Event<RegistryEntryAddedCallback<T>> fabric$getEntryAddedCallback() {
		if (this.fabric_addObjectEvent == null) {
			fabric_addObjectEvent = EventFactory.createArrayBacked(RegistryEntryAddedCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryEntryAddedCallback<T> callback : callbacks) {
							callback.onEntryAdded(rawId, id, object);
						}
					}
			);
		}

		return this.fabric_addObjectEvent;
	}

	@Override
	public Event<RegistryBeforeAddCallback<T>> fabric$getBeforeAddedCallback() {
		if (this.fabric_beforeAddObjectEvent == null) {
			fabric_beforeAddObjectEvent = EventFactory.createArrayBacked(RegistryBeforeAddCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryBeforeAddCallback<T> callback : callbacks) {
							callback.onEntryAdding(rawId, id, object);
						}
					}
			);
		}

		return this.fabric_beforeAddObjectEvent;
	}

	@Inject(method = "register(ILnet/ornithemc/osl/registries/api/registry/ResourceKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("HEAD"))
	private void fabric$preregisterEvent(int id, ResourceKey<T> key, T value, CallbackInfoReturnable<T> cir) {
		fabric$getBeforeAddedCallback().invoker().onEntryAdding(id, Identifier.fromNamespaceIdentifier(key.identifier()), value);
	}

	@Inject(method = "register(ILnet/ornithemc/osl/registries/api/registry/ResourceKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("RETURN"))
	private void fabric$postregisterEvent(int id, ResourceKey<T> key, T value, CallbackInfoReturnable<T> cir) {
		fabric$getEntryAddedCallback().invoker().onEntryAdded(id, Identifier.fromNamespaceIdentifier(key.identifier()), value);
	}
}
