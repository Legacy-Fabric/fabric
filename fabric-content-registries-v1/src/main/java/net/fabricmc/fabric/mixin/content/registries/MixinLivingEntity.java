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

package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.content.registry.v1.block.Climbable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	public MixinLivingEntity(World world) {
		super(world);
	}

	@Inject(at = @At("RETURN"), method = "method_7166", cancellable = true)
	public void climb(CallbackInfoReturnable<Boolean> cir) {
		BlockPos pos = new BlockPos(MathHelper.floor(this.x), MathHelper.floor(this.getBoundingBox().y1), MathHelper.floor(this.z));
		BlockState blockState = this.world.getBlockState(pos);

		if (blockState.getBlock() instanceof Climbable) {
			cir.setReturnValue(((Climbable) blockState.getBlock()).canClimb(blockState, this.world, pos, (LivingEntity) (Object) this));
		}
	}
}
