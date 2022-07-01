/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T, I> implements SimpleRegistryCompat<T, I> {
	@Mutable
	@Shadow
	@Final
	protected IdList ids;

	@Shadow
	@Final
	protected Map<I, T> objects;

	@Shadow
	public abstract int getIndex(I object);

	@Override
	public IdListCompat<I> getIds() {
		return (IdListCompat<I>) this.ids;
	}

	@Override
	public Map<I, T> getObjects() {
		return this.objects;
	}

	@Override
	public void setIds(IdListCompat<I> idList) {
		this.ids = (IdList) idList;
	}

	@Override
	public IdListCompat<I> createIdList() {
		return (IdListCompat<I>) new IdList();
	}

	@Override
	public int getRawID(I object) {
		return this.getIndex(object);
	}
}
