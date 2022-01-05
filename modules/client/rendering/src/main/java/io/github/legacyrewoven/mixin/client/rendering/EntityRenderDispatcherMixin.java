/*
 * Copyright (c) 2021 Legacy Rewoven
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

package io.github.legacyrewoven.mixin.client.rendering;

import java.util.Map;

import io.github.legacyrewoven.api.client.rendering.v1.EntityRendererRegistry;
import io.github.legacyrewoven.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	@Shadow	private Map<Class<? extends Entity>, EntityRenderer> renderers;
	@Shadow public TextureManager textureManager;
	//TODO: Make this something more useful.
	private ItemRenderer itemRenderer = new ItemRenderer();

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void afterRegisterRenderers(CallbackInfo ci) {
		final EntityRenderDispatcher me = (EntityRenderDispatcher) (Object) this;
		EntityRendererRegistry.INSTANCE.initialize(me, this.textureManager, MinecraftClient.getInstance().getResourceManager(), itemRenderer, renderers);

		for (Map.Entry<Class<? extends Entity>, EntityRenderer> entry : this.renderers.entrySet()) {
			if (entry.getValue() instanceof LivingEntityRenderer) {
				LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor) entry.getValue();

				/**Sorry, features are built into the entity renderer in versions < 1.8.*/
				LivingEntityFeatureRendererRegistrationCallback.EVENT.invoker().registerRenderers((Class<? extends LivingEntity>) entry.getKey(), (LivingEntityRenderer) entry.getValue());
			}
		}
	}
}
