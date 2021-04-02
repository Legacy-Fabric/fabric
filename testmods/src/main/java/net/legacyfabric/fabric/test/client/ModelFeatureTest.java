package net.legacyfabric.fabric.test.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.legacyfabric.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.entity.LivingEntity;

import net.fabricmc.api.ClientModInitializer;

public class ModelFeatureTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityClass, entityRenderer, registrationHelper) -> {
			if (entityRenderer instanceof PlayerEntityRenderer) {
				registrationHelper.register(new FeatureRenderer<LivingEntity>() {
					private final BlockState state = Blocks.DIRT.getDefaultState();

					@Override
					public void render(LivingEntity entity, float handSwing, float handSwingAmount, float tickDelta, float age, float headYaw, float headPitch, float scale) {
						GlStateManager.pushMatrix();
						GlStateManager.translatef(0.0F, 1.0F, 0.0F);
						GlStateManager.scalef(2, 2, 2);
						MinecraftClient.getInstance().getBlockRenderManager().method_3590(state, state.getBlock().getColor(state));
						GlStateManager.popMatrix();
					}

					@Override
					public boolean combineTextures() {
						return false;
					}
				});
			}
		});
	}
}
