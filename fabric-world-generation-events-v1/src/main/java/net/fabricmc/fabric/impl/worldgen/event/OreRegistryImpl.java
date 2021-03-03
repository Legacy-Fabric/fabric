/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.impl.worldgen.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.fabric.api.worldgen.event.v1.OreEntry;
import net.fabricmc.fabric.api.worldgen.event.v1.OreRegistry;

public class OreRegistryImpl implements OreRegistry, Iterable<OreEntry> {
	private final Set<OreEntry> entries;

	public OreRegistryImpl() {
		this.entries = new HashSet<>();
	}

	public void register(int count, OreFeature feature, int minHeight, int maxHeight) {
		this.entries.add(new OreEntry(count, feature, minHeight, maxHeight));
	}

	public void forEach(@Nonnull OreConsumer action) {
		Preconditions.checkNotNull(action, "action was null");

		for (OreEntry e : this) {
			action.accept(e.getCount(), e.getFeature(), e.getMaxHeight(), e.getMinHeight());
		}
	}

	@Nonnull
	@Override
	public Iterator<OreEntry> iterator() {
		return this.entries.iterator();
	}

	@FunctionalInterface
	public interface OreConsumer {
		void accept(int count, OreFeature feature, int maxHeight, int minHeight);
	}
}
