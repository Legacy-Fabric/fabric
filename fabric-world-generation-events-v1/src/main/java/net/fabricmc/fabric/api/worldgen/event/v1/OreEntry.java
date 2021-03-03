/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.api.worldgen.event.v1;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import net.minecraft.world.gen.feature.OreFeature;

/**
 * Data class that encapsulates ore generation data.
 */
public class OreEntry {
	private final int count;
	private final OreFeature feature;
	private final int minHeight;
	private final int maxHeight;

	public OreEntry(int count, OreFeature feature, int minHeight, int maxHeight) {
		this.count = count;
		this.feature = feature;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	public int getCount() {
		return this.count;
	}

	public OreFeature getFeature() {
		return this.feature;
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("count", this.count)
				.append("feature", this.feature)
				.append("minHeight", this.minHeight)
				.append("maxHeight", this.maxHeight)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		OreEntry oreEntry = (OreEntry) o;
		return this.count == oreEntry.count && this.minHeight == oreEntry.minHeight && this.maxHeight == oreEntry.maxHeight && Objects.equals(this.feature, oreEntry.feature);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.count, this.feature, this.minHeight, this.maxHeight);
	}
}
