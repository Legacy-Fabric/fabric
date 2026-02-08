/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.test.client.rendering;

import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.layer.EntityRenderLayer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.living.LivingEntity;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

public class ModelFeatureTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityClass, entityRenderer, registrationHelper) -> {
			if (entityRenderer instanceof PlayerRenderer) {
				registrationHelper.register(new EntityRenderLayer<LivingEntity>() {
					private final BlockState state = Blocks.DIRT.defaultState();

					@Override
					public void render(LivingEntity entity, float handSwing, float handSwingAmount, float tickDelta, float age, float headYaw, float headPitch, float scale) {
						GlStateManager.pushMatrix();
						GlStateManager.enableTexture();
						GlStateManager.translatef(0.0F, 1.0F, 0.0F);
						GlStateManager.scalef(2, 2, 2);
						Minecraft.getInstance().getBlockRenderDispatcher().renderAsItem(state, 9923917);
						GlStateManager.disableTexture();
						GlStateManager.popMatrix();
					}

					@Override
					public boolean colorsWhenDamaged() {
						return false;
					}
				});
			}
		});
	}
}
