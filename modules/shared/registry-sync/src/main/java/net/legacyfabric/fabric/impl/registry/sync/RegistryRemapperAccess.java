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

package net.legacyfabric.fabric.impl.registry.sync;

import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public interface RegistryRemapperAccess {
	Identifier PACKET_ID = new Identifier("legacy-fabric-api", "registry_remap");

	RegistryRemapper getItemRemapper();

	RegistryRemapper getBlockRemapper();

	default void remap() {
		this.getItemRemapper().remap();
		this.getBlockRemapper().remap();
	}

	default void readAndRemap(CompoundTag tag) {
		this.getItemRemapper().fromTag(tag.getCompound("Items"));
		this.getBlockRemapper().fromTag(tag.getCompound("Blocks"));
		this.remap();
	}

	default CompoundTag toRegistryTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("Items", this.getItemRemapper().toTag());
		tag.put("Blocks", this.getBlockRemapper().toTag());
		return tag;
	}

	default PacketByteBuf createBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCompoundTag(this.toRegistryTag());
		return buf;
	}
}
