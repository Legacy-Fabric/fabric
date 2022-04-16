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

package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelProperties;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements RegistryRemapperAccess {
	@Unique
	private RegistryRemapper itemRemapper = new RegistryRemapper(Item.REGISTRY);
	@Unique
	private RegistryRemapper blockRemapper = new RegistryRemapper(Block.REGISTRY);

	@Override
	public RegistryRemapper getItemRemapper() {
		return itemRemapper;
	}

	@Override
	public RegistryRemapper getBlockRemapper() {
		return blockRemapper;
	}

	@Inject(at = @At("TAIL"), method = {"<init>()V", "<init>(Lnet/minecraft/world/level/LevelInfo;Ljava/lang/String;)V"})
	public void init(CallbackInfo ci) {
		this.itemRemapper.dump();
		this.blockRemapper.dump();
	}


	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V")
	public void init(CompoundTag worldNbt, CallbackInfo ci) {
		this.itemRemapper.fromTag(worldNbt.getCompound("FabricItemRegistry"));
		this.blockRemapper.fromTag(worldNbt.getCompound("FabricBlockRegistry"));
	}

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/LevelProperties;)V")
	public void init(LevelProperties worldNbt, CallbackInfo ci) {
		this.itemRemapper = ((RegistryRemapperAccess) worldNbt).getItemRemapper().copy();
		this.blockRemapper = ((RegistryRemapperAccess) worldNbt).getBlockRemapper().copy();
	}
}
