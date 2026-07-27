/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

import net.ornithemc.osl.registries.api.registry.Registry;

public class RegistryHelperImplementation {
	public static final Identifier PACKET_ID = new Identifier("lf-api:registry");
	private static final Map<NamespacedIdentifier, Event<RegistryInitializedEvent>> INITIALIZATION_EVENTS = new HashMap<>();
	private static final Map<NamespacedIdentifier, FabricRegistry<?>> REGISTRIES = new HashMap<>();

	public static Event<RegistryInitializedEvent> getInitializationEvent(NamespacedIdentifier registryId) {
		Event<RegistryInitializedEvent> event;

		if (INITIALIZATION_EVENTS.containsKey(registryId)) {
			event = INITIALIZATION_EVENTS.get(registryId);
		} else {
			event = EventFactory.createArrayBacked(RegistryInitializedEvent.class,
					(callbacks) -> new RegistryInitializedEvent() {
						@Override
						public <T> void initialized(FabricRegistry<T> registry) {
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

//	public static <T> void registerRegistry(NamespacedIdentifier identifier, FabricRegistry<T> holder) {
//		if (REGISTRIES.containsKey(identifier)) throw new IllegalArgumentException("Attempted to register registry " + identifier.toString() + " twices!");
//		REGISTRIES.put(identifier, holder);
//
//		Identifier compatIdentifier = Identifier.fromNamespaceIdentifier(identifier);
//
//		if (holder instanceof RegistryIdSetter) ((RegistryIdSetter) holder).fabric$setId(compatIdentifier);
//
//		boolean remappable = true;
//
//		if (holder instanceof DesynchronizeableRegistrable) {
//			remappable = ((DesynchronizeableRegistrable) holder).fabric$canSynchronize();
//		}
//
//		if (holder instanceof SyncedRegistrableFabricRegistry && remappable) {
//			REMAPPERS.put(identifier, new RegistryRemapper<>((SyncedRegistrableFabricRegistry<?>) holder));
//		}
//
//		getInitializationEvent(identifier).invoker().initialized(holder);
//
//		holder.fabric$getBeforeAddedCallback().register((rawId, id, object) -> {
//			Event<RegistryBeforeAddCallback<T>> event = (Event<RegistryBeforeAddCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_BEFORE_MAP.get(identifier);
//
//			if (event != null) event.invoker().onEntryAdding(rawId, id, object);
//		});
//
//		holder.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
//			Event<RegistryEntryAddedCallback<T>> event = (Event<RegistryEntryAddedCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_ADDED_MAP.get(identifier);
//
//			if (event != null) event.invoker().onEntryAdded(rawId, id, object);
//		});
//
//		if (holder instanceof SyncedRegistrableFabricRegistry && remappable) {
//			((SyncedRegistrableFabricRegistry<T>) holder).fabric$getRegistryRemapCallback().register(changedIdsMap -> {
//				Event<RegistryRemapCallback<T>> event = (Event<RegistryRemapCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_REMAP_MAP.get(identifier);
//
//				if (event != null) event.invoker().callback(changedIdsMap);
//			});
//		}
//	}

	public static <T> void register(Registry<T> registry, NamespacedIdentifier identifier, T value) {
		Registry.register(registry, identifier, value);
	}

	public static <T> T register(Registry<T> registry, NamespacedIdentifier identifier, Function<Integer, T> valueConstructor) {
		T value = valueConstructor.apply(AUTO_ASSIGN_ID_MAP.get(registry));
		register(registry, identifier, value);
		return value;
	}

	private static final Map<String, String> BACKWARD_COMPATIBILITY = new HashMap<>();
	static {
		BACKWARD_COMPATIBILITY.put("Items", RegistryIds.ITEMS.toString());
		BACKWARD_COMPATIBILITY.put("Blocks", RegistryIds.BLOCKS.toString());
		BACKWARD_COMPATIBILITY.put("Biomes", RegistryIds.BIOMES.toString());
		BACKWARD_COMPATIBILITY.put("BlockEntityTypes", RegistryIds.BLOCK_ENTITY_TYPES.toString());
		BACKWARD_COMPATIBILITY.put("Enchantments", RegistryIds.ENCHANTMENTS.toString());
		BACKWARD_COMPATIBILITY.put("EntityTypes", RegistryIds.ENTITY_TYPES.toString());
		BACKWARD_COMPATIBILITY.put("StatusEffects", RegistryIds.STATUS_EFFECTS.toString());
	}

	public static String convertRegistryId(String key) {
		if (BACKWARD_COMPATIBILITY.containsKey(key)) {
			key = BACKWARD_COMPATIBILITY.get(key);
		}

		return key.substring(0, key.length() - 1);
	}

	private static final Map<NamespacedIdentifier, Registry<?>> ID2REGISTRY = new HashMap<>();
	private static final Map<FabricRegistry<?>, Registry<?>> REGISTRY2REGISTRY = new HashMap<>();
	private static final Map<Registry<?>, Integer> AUTO_ASSIGN_ID_MAP = new HashMap<>();

	public static void registerCompatId(NamespacedIdentifier id, Registry<?> registry) {
		ID2REGISTRY.put(id, registry);
	}

	public static <T> void registerCompatRegistry(FabricRegistry<T> oldRegistry, Registry<T> registry) {
		REGISTRY2REGISTRY.put(oldRegistry, registry);
	}

	public static void registerAutoAssign(Registry<?> id, int autoId) {
		AUTO_ASSIGN_ID_MAP.put(id, autoId);
	}

	public static <T> Registry<T> getRegistryCompat(NamespacedIdentifier identifier) {
		return (Registry<T>) ID2REGISTRY.get(identifier);
	}

	public static <T> Registry<T> getRegistryCompat(FabricRegistry<T> registry) {
		return (Registry<T>) REGISTRY2REGISTRY.get(registry);
	}
}
