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

package net.fabricmc.fabric.mixin.event.lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

@Mixin(LightningBoltEntity.class)
public class MixinLightningBoltEntity {
	@Inject(at = @At("TAIL"), method = "<init>")
	public void onLightningStrike(World world, double d, double e, double f, CallbackInfo info) {
		ServerEntityEvents.LIGHTNING_STRIKE.invoker().onLightningStrike((LightningBoltEntity) (Object) this, world, d, e, f);
	}
}
