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

package net.legacyfabric.fabric.mixin.networking.versioned;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.PacketByteBuf;

import net.legacyfabric.fabric.impl.networking.PacketByteBufExtension;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufMixin implements PacketByteBufExtension {
	@Shadow
	public abstract PacketByteBuf writeNbtCompound(@Nullable NbtCompound nbt);

	@Override
	public PacketByteBuf writeCompound(NbtCompound compound) {
		return writeNbtCompound(compound);
	}
}
