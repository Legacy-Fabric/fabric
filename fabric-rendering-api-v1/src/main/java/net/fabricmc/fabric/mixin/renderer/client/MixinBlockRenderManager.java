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

package net.fabricmc.fabric.mixin.renderer.client;

import org.apache.commons.lang3.tuple.MutablePair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.impl.renderer.DamageModel;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderManager.class)
public abstract class MixinBlockRenderManager {
	@Shadow
	@Final
	private BlockModelRenderer blockModelRenderer;
	private static final ThreadLocal<MutablePair<DamageModel, BakedModel>> DAMAGE_STATE = ThreadLocal.withInitial(() -> MutablePair.of(new DamageModel(), null));

	@ModifyVariable(method = "method_3594", at = @At(value = "STORE", ordinal = 0), allow = 1, require = 1)
	private BakedModel hookTesselateDamageModel(BakedModel modelIn) {
		DAMAGE_STATE.get().right = modelIn;
		return modelIn;
	}

	@Inject(method = "method_3594", cancellable = true,
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/class_1047;method_3585(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
	private void hookTesselateDamage(BlockState blockState, BlockPos blockPos, Sprite sprite, WorldView blockView, CallbackInfo ci) {
		MutablePair<DamageModel, BakedModel> damageState = DAMAGE_STATE.get();

		if (damageState.right != null && !((FabricBakedModel) damageState.right).isVanillaAdapter()) {
			damageState.left.prepare(damageState.right, sprite, blockState, blockPos);
			this.blockModelRenderer.method_3602(blockView, damageState.left, blockState.getBlock(), blockPos, Tessellator.getInstance().getBuffer(), true);
			ci.cancel();
		}
	}
}
