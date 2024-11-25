/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.PacketByteBuf;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.SyncedRegistrableFabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.DesynchronizeableRegistrable;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.Registrable;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.RegistryEntryCreator;
import net.legacyfabric.fabric.api.registry.v2.registry.registrable.SyncedRegistrable;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.networking.PacketByteBufExtension;
import net.legacyfabric.fabric.impl.registry.accessor.RegistryIdSetter;

public class RegistryHelperImplementation {
	public static final Identifier PACKET_ID = new Identifier("legacy-fabric-api:registry_remap");
	public static final boolean hasFlatteningBegun = VersionUtils.matches(">=1.8 <=1.12.2");
	public static final Map<Identifier, Event<RegistryInitializedEvent>> INITIALIZATION_EVENTS = new HashMap<>();
	private static final Map<Identifier, FabricRegistry<?>> REGISTRIES = new HashMap<>();
	private static final Map<Identifier, RegistryRemapper<?>> REMAPPERS = new HashMap<>();
	private static final List<Consumer<Identifier>> REGISTRY_REGISTERED = new ArrayList<>();

	public static void registerRegisterEvent(Consumer<Identifier> callback) {
		REGISTRY_REGISTERED.add(callback);
	}

	public static Event<RegistryInitializedEvent> getInitializationEvent(Identifier registryId) {
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

	public static <T> FabricRegistry<T> getRegistry(Identifier identifier) {
		return (FabricRegistry<T>) REGISTRIES.get(identifier);
	}

	public static <T> void registerRegistry(Identifier identifier, FabricRegistry<T> holder) {
		if (REGISTRIES.containsKey(identifier)) throw new IllegalArgumentException("Attempted to register registry " + identifier.toString() + " twices!");
		REGISTRIES.put(identifier, holder);

		if (holder instanceof RegistryIdSetter) ((RegistryIdSetter) holder).fabric$setId(identifier);

		boolean remappable = true;

		if (holder instanceof DesynchronizeableRegistrable) {
			remappable = ((DesynchronizeableRegistrable) holder).canSynchronize();
		}

		if (holder instanceof SyncedRegistrableFabricRegistry && remappable) {
			REMAPPERS.put(identifier, new RegistryRemapper<>((SyncedRegistrableFabricRegistry<?>) holder));
		}

		REGISTRY_REGISTERED.forEach(c -> c.accept(identifier));

		getInitializationEvent(identifier).invoker().initialized(holder);

		holder.fabric$getBeforeAddedCallback().register((rawId, id, object) -> {
			Event<RegistryBeforeAddCallback<T>> event = (Event<RegistryBeforeAddCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_BEFORE_MAP.get(identifier);

			if (event != null) event.invoker().onEntryAdding(rawId, id, object);
		});

		holder.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
			Event<RegistryEntryAddedCallback<T>> event = (Event<RegistryEntryAddedCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_ADDED_MAP.get(identifier);

			if (event != null) event.invoker().onEntryAdded(rawId, id, object);
		});

		if (holder instanceof SyncedRegistrableFabricRegistry && remappable) {
			((SyncedRegistrableFabricRegistry<T>) holder).fabric$getRegistryRemapCallback().register(changedIdsMap -> {
				Event<RegistryRemapCallback<T>> event = (Event<RegistryRemapCallback<T>>) (Object) RegistryEventHelper.IDENTIFIER_REMAP_MAP.get(identifier);

				if (event != null) event.invoker().callback(changedIdsMap);
			});
		}
	}

	public static <T> void register(FabricRegistry<T> registry, Identifier identifier, T value) {
		if (registry == null) throw new IllegalArgumentException("Can't register to a null registry!");
		if (!(registry instanceof Registrable)) throw new IllegalArgumentException("Can't register object to non registrable registry " + registry.fabric$getId());

		Registrable<T> registrable = (Registrable<T>) registry;
		int computedId = -1;

		boolean remappable = true;

		if (registry instanceof DesynchronizeableRegistrable) {
			remappable = ((DesynchronizeableRegistrable) registry).canSynchronize();
		}

		if (registry instanceof SyncedRegistrable && remappable) {
			computedId = ((SyncedRegistrable<T>) registrable).fabric$nextId();
		}

		registrable.fabric$register(computedId, identifier, value);
	}

	public static <T> T register(FabricRegistry<T> registry, Identifier identifier, Function<Integer, T> valueConstructor) {
		if (registry == null) throw new IllegalArgumentException("Can't register to a null registry!");
		if (!(registry instanceof SyncedRegistrable)) throw new IllegalArgumentException("Can't register object to non registrable registry " + registry.fabric$getId());

		SyncedRegistrable<T> registrable = (SyncedRegistrable<T>) registry;
		int computedId = registrable.fabric$nextId();

		T value = valueConstructor.apply(computedId);

		registrable.fabric$register(computedId, identifier, value);

		return value;
	}

	public static <T> RegistryEntryCreator<T> createEntryCreator(Identifier identifier, Function<Integer, T> valueConstructor, int offset) {
		return new RegistryEntryCreatorImpl<>(identifier, offset, valueConstructor);
	}

	@SafeVarargs
	public static <T> List<FabricRegistryEntry<T>> register(FabricRegistry<T> registry, RegistryEntryCreator<T>... entryCreators) {
		if (entryCreators.length < 1) throw new IllegalArgumentException("Can't register nothing to a registry!");
		if (registry == null) throw new IllegalArgumentException("Can't register to a null registry!");
		if (!(registry instanceof SyncedRegistrable) || !(registry instanceof SyncedFabricRegistry)) throw new IllegalArgumentException("Can't register object to non registrable registry " + registry.fabric$getId());

		SyncedRegistrableFabricRegistry<T> registrable = (SyncedRegistrableFabricRegistry<T>) registry;
		int baseComputedId = registrable.fabric$nextId();

		Map<Integer, Integer> computedIdsMap = new HashMap<>();

		boolean matchPredicate = false;

		while (!matchPredicate) {
			for (RegistryEntryCreator<T> creator : entryCreators) {
				int offset = creator.getIdOffset();
				int offsetId = baseComputedId + offset;

				if (registrable.fabric$getValue(offsetId) == null) {
					matchPredicate = true;
					computedIdsMap.put(offset, offsetId);
				} else {
					matchPredicate = false;
					computedIdsMap.clear();

					do {
						baseComputedId++;
					} while (registrable.fabric$getValue(baseComputedId) != null);

					break;
				}
			}
		}

		List<FabricRegistryEntry<T>> entries = new ArrayList<>();

		for (RegistryEntryCreator<T> creator : entryCreators) {
			int computedId = computedIdsMap.get(creator.getIdOffset());

			FabricRegistryEntry<T> registryEntry = new FabricRegistryEntryImpl<>(computedId, creator.getIdentifier(), creator.getValue(computedId));
			entries.add(registryEntry);

			registrable.fabric$register(registryEntry.getId(), registryEntry.getIdentifier(), registryEntry.getValue());
		}

		return entries;
	}

	public static void remapRegistries() {
		for (RegistryRemapper<?> remapper : REMAPPERS.values()) {
			remapper.remap();
		}
	}

	public static void readAndRemap(NbtCompound compound) {
		for (String key : compound.getKeys()) {
			String registryKey = key;

			if (BACKWARD_COMPATIBILITY.containsKey(key)) {
				registryKey = BACKWARD_COMPATIBILITY.get(key);
			}

			Identifier identifier = new Identifier(registryKey);
			RegistryRemapper<?> remapper = REMAPPERS.get(identifier);

			if (remapper != null) {
				remapper.readNbt(compound.getCompound(key));
			}
		}

		remapRegistries();
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

	public static NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();

		for (Map.Entry<Identifier, RegistryRemapper<?>> entry : REMAPPERS.entrySet()) {
			compound.put(entry.getKey().toString(), entry.getValue().toNbt());
		}

		return compound;
	}

	public static PacketByteBuf createBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		return ((PacketByteBufExtension) buf).writeCompound(toNbt());
	}
}
