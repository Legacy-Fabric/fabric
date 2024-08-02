/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import net.fabricmc.api.ClientModInitializer;

public class ModelFeatureTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityClass, entityRenderer, registrationHelper) -> {
		//			if (entityRenderer instanceof PlayerEntityRenderer) {
		//				registrationHelper.register(new FeatureRenderer() {
		//					private final BlockState state = Blocks.DIRT.getDefaultState();
		//
		//					@Override
		//					public void render(LivingEntity entity, float handSwing, float handSwingAmount, float tickDelta, float age, float headYaw, float headPitch, float scale) {
		//						GlStateManager.pushMatrix();
		//						GlStateManager.enableTexture();
		//						GlStateManager.translate(0.0F, 1.0F, 0.0F);
		//						GlStateManager.scale(2, 2, 2);
		//						MinecraftClient.getInstance().getBlockRenderManager().renderBlockEntity(state, 9923917);
		//						GlStateManager.disableTexture();
		//						GlStateManager.popMatrix();
		//					}
		//
		//					@Override
		//					public boolean combineTextures() {
		//						return false;
		//					}
		//				});
		//			}
		//		});
	}
}
