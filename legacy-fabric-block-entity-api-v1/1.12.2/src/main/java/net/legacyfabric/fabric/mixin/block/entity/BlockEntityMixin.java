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

package net.legacyfabric.fabric.mixin.block.entity;

import net.legacyfabric.fabric.api.block.entity.v1.BlockEntityEvents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
	@Shadow
	private static void register(String par1, Class<? extends BlockEntity> par2) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		BlockEntityEvents.REGISTER_BLOCK_ENTITIES.invoker().accept((id, clazz) -> register(id.toString(), clazz));
	}
}
