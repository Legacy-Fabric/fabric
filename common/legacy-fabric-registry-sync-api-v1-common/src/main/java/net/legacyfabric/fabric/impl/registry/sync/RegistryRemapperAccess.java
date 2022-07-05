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

package net.legacyfabric.fabric.impl.registry.sync;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.PacketByteBuf;

import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.sync.compat.PacketByteBufCompat;
import net.legacyfabric.fabric.impl.registry.sync.remappers.BlockEntityRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.remappers.BlockRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.remappers.ItemRegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;

public interface RegistryRemapperAccess {
	Identifier PACKET_ID = new Identifier("legacy-fabric-api:registry_remap");

	RegistryRemapper<RegistryRemapper<?>> getRegistryRemapperRegistryRemapper();

	default RegistryRemapper<?>[] createDefaultRegistryRemappers() {
		return new RegistryRemapper[] {
				new ItemRegistryRemapper(),
				new BlockRegistryRemapper(),
				new BlockEntityRegistryRemapper()
		};
	}

	default void remap() {
		this.getRegistryRemapperRegistryRemapper().remap();

		for (RegistryRemapper<?> registryRemapper : this.getRegistryRemapperRegistryRemapper().getRegistry()) {
			registryRemapper.remap();
		}
	}

	default void readAndRemap(NbtCompound nbt) {
		this.getRegistryRemapperRegistryRemapper().readNbt(nbt.getCompound(this.getRegistryRemapperRegistryRemapper().nbtName));

		for (RegistryRemapper<?> registryRemapper : this.getRegistryRemapperRegistryRemapper().getRegistry()) {
			registryRemapper.readNbt(nbt.getCompound(registryRemapper.nbtName));
		}

		this.remap();
	}

	default NbtCompound toNbtCompound() {
		NbtCompound nbt = new NbtCompound();
		nbt.put(this.getRegistryRemapperRegistryRemapper().nbtName, this.getRegistryRemapperRegistryRemapper().toNbt());

		for (RegistryRemapper<?> registryRemapper : this.getRegistryRemapperRegistryRemapper().getRegistry()) {
			nbt.put(registryRemapper.nbtName, registryRemapper.toNbt());
		}

		return nbt;
	}

	default PacketByteBuf createBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		return ((PacketByteBufCompat) buf).writeCompound(this.toNbtCompound());
	}
}
