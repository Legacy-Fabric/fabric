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

package net.legacyfabric.fabric.mixin.client.rendering;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import net.legacyfabric.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.legacyfabric.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.legacyfabric.fabric.impl.client.rendering.RegistrationHelperImpl;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	@Shadow
	private Map<Class<? extends Entity>, EntityRenderer<?>> renderers;

	@Shadow
	private Map<String, PlayerEntityRenderer> modelRenderers;

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void afterRegisterRenderers(TextureManager textureManager, ItemRenderer itemRenderer, CallbackInfo ci) {
		final EntityRenderDispatcher me = (EntityRenderDispatcher) (Object) this;
		EntityRendererRegistry.INSTANCE.initialize(me, textureManager, MinecraftClient.getInstance().getResourceManager(), itemRenderer, renderers);

		// Dispatch events to register feature renderers.
		for (Map.Entry<Class<? extends Entity>, EntityRenderer<?>> entry : this.renderers.entrySet()) {
			if (entry.getValue() instanceof LivingEntityRenderer) { // Must be living for features
				LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor) entry.getValue();

				LivingEntityFeatureRendererRegistrationCallback.EVENT.invoker().registerRenderers((Class<? extends LivingEntity>) entry.getKey(), (LivingEntityRenderer) entry.getValue(), new RegistrationHelperImpl(accessor::callAddFeature));
			}
		}

		// Players are a fun case, we need to do these separately and per model type
		for (Map.Entry<String, PlayerEntityRenderer> entry : this.modelRenderers.entrySet()) {
			LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor) entry.getValue();

			LivingEntityFeatureRendererRegistrationCallback.EVENT.invoker().registerRenderers(AbstractClientPlayerEntity.class, entry.getValue(), new RegistrationHelperImpl(accessor::callAddFeature));
		}
	}
}
