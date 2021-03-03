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

package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.content.registry.v1.PlayerBlockHarvestListener;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
	@Shadow
	public World world;

	@Shadow
	public ServerPlayerEntity player;

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;method_6096(Lnet/minecraft/util/math/BlockPos;)Z"), method = "method_6094")
	public boolean modifyCanBreakBlock(boolean bl, BlockPos pos) {
		if (this.world.getBlockState(pos).getBlock() instanceof PlayerBlockHarvestListener) {
			return ((PlayerBlockHarvestListener) this.world.getBlockState(pos).getBlock()).canHarvest(this.world, pos, this.player);
		}

		return bl;
	}
}
