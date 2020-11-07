package net.fabricmc.fabric.impl.worldgen.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.fabric.api.worldgen.event.v1.OreRegistry;

public class OreRegistryImpl implements OreRegistry, Iterable<OreRegistryImpl.Entry> {
	private final Set<Entry> entries;

	public OreRegistryImpl() {
		this.entries = new HashSet<>();
	}

	public void register(int count, OreFeature feature, int minHeight, int maxHeight) {
		this.entries.add(new Entry(count, feature, minHeight, maxHeight));
	}

	public void forEach(Consumer4 consumer) {
		Preconditions.checkNotNull(consumer, "action was null");
		for (Entry e : this) {
			consumer.accept(e.count, e.feature, e.maxHeight, e.minHeight);
		}
	}

	@Nonnull
	@Override
	public Iterator<Entry> iterator() {
		return this.entries.iterator();
	}

	static class Entry {
		private final int count;
		private final OreFeature feature;
		private final int minHeight;
		private final int maxHeight;

		private Entry(int count, OreFeature feature, int minHeight, int maxHeight) {
			this.count = count;
			this.feature = feature;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
		}
	}

	@FunctionalInterface
	public interface Consumer4 {
		void accept(int count, OreFeature feature, int maxHeight, int minHeight);
	}
}
