package net.legacyfabric.fabric.impl.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.legacyfabric.fabric.mixin.registry.IdListAccessor;
import net.legacyfabric.fabric.mixin.registry.SimpleRegistryAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.function.IntSupplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

public class RegistryRemapper {
	private static final Logger LOGGER = LogManager.getLogger();
	private final SimpleRegistry<Identifier, ?> registry;
	private BiMap<Identifier, Integer> entryDump;

	public RegistryRemapper(SimpleRegistry<Identifier, ?> registry) {
		this.registry = registry;
	}

	public void dump() {
		this.entryDump = HashBiMap.create();
		getIdMap(this.registry).forEach((value, id) -> {
			Identifier key = this.getIdentifier(value);
			this.entryDump.put(key, id);
		});
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		this.entryDump.forEach((key, value) -> tag.putInt(key.toString(), value));
		return tag;
	}

	public void fromTag(CompoundTag tag) {
		this.entryDump = HashBiMap.create();
		for (String key : tag.getKeys()) {
			Identifier identifier = new Identifier(key);
			int id = tag.getInt(key);
			this.entryDump.put(identifier, id);
		}
	}

	// Type erasure, ily
	public void remap() {
		LOGGER.info("Remapping registry");
		IdList newList = new IdList<>();
		this.entryDump.forEach((id, rawId) -> {
			Object value = Objects.requireNonNull(getValue(id));
			newList.set(value, rawId);
		});
		IntSupplier currentSize = () -> getIdMap(newList).size();
		IntSupplier previousSize = () -> getObjects(this.registry).size();
		if (currentSize.getAsInt() > previousSize.getAsInt()) {
			throw new IllegalStateException("Registry size increased from " + previousSize + " to " + currentSize + " after remapping! This is not possible!");
		} else if (currentSize.getAsInt() < previousSize.getAsInt()) {
			LOGGER.info("Adding " + (previousSize.getAsInt() - currentSize.getAsInt()) + " missing entries to registry");
			getObjects(this.registry).keySet().stream().filter(obj -> newList.getId(obj) == -1).forEach(missing -> {
				int id = nextId();
				newList.set(missing, id);
			});
		}
		if (currentSize.getAsInt() != previousSize.getAsInt()) {
			throw new IllegalStateException("An error occured during remapping");
		}
		((SimpleRegistryAccessor) this.registry).setIds(newList);
		this.dump();
		LOGGER.info("Remapped "  + previousSize + " entries");
	}

	public int nextId() {
		int id = 0;
		while (this.registry.byIndex(id) != null) {
			id++;
		}
		return id;
	}

	public Object getValue(Identifier id) {
		return getObjects(this.registry).inverse().get(id);
	}

	public Identifier getIdentifier(Object thing) {
		return getObjects(this.registry).get(thing);
	}

	public static IdList<?> getIdList(SimpleRegistry<Identifier, ?> registry) {
		return ((SimpleRegistryAccessor) registry).getIds();
	}

	public static BiMap<?, Identifier> getObjects(SimpleRegistry<Identifier, ?> registry) {
		//noinspection unchecked
		return (BiMap<?, Identifier>) ((SimpleRegistryAccessor) registry).getObjects();
	}

	public static IdentityHashMap<?, Integer> getIdMap(IdList<?> idList) {
		return ((IdListAccessor) idList).getIdMap();
	}

	public static IdentityHashMap<?, Integer> getIdMap(SimpleRegistry<Identifier, ?> registry) {
		return getIdMap(getIdList(registry));
	}

	public RegistryRemapper copy() {
		RegistryRemapper remapper = new RegistryRemapper(this.registry);
		remapper.entryDump = HashBiMap.create(this.entryDump);
		return remapper;
	}
}
