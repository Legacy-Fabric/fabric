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

package io.github.legacyrewoven.api.client.rendering.v1;

import io.github.legacyrewoven.api.event.Event;
import io.github.legacyrewoven.api.event.EventFactory;

import net.minecraft.client.render.entity.EntityRenderer;
//import net.minecraft.client.render.entity.feature.Deadmau5FeatureRenderer;
//import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.entity.LivingEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Called when {@link FeatureRenderer feature renderers} for a {@link LivingEntityRenderer living entity renderer} are registered.
 *
 * <p>Feature renderers are typically used for rendering additional objects on an entity, such as armor, an elytra or {@link Deadmau5FeatureRenderer Deadmau5's ears}.
 * This callback lets developers add additional feature renderers for use in entity rendering.
 * Listeners should filter out the specific entity renderer they want to hook into, usually through {@code instanceof} checks or filtering by entity type.
 * Once listeners find a suitable entity renderer, they should register their feature renderer via the registration helper.
 *
 * <p>For example, to register a feature renderer for a player model, the example below may used:
 * <blockquote><pre>
 * LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper) -> {
 * 	if (entityRenderer instanceof PlayerEntityModel) {
 * 		registrationHelper.register(new MyFeatureRenderer((PlayerEntityModel) entityRenderer));
 *    }
 * });
 * </pre></blockquote>
 */
@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface LivingEntityFeatureRendererRegistrationCallback {
	Event<LivingEntityFeatureRendererRegistrationCallback> EVENT = EventFactory.createArrayBacked(LivingEntityFeatureRendererRegistrationCallback.class, callbacks -> (entityClass, entityRenderer, registrationHelper) -> {
		for (LivingEntityFeatureRendererRegistrationCallback callback : callbacks) {
			callback.registerRenderers(entityClass, entityRenderer, registrationHelper);
		}
	});

	/**
	 * Called when feature renderers may be registered.
	 *
	 * @param entityClass    the entity class
	 * @param entityRenderer the entity renderer
	 */
	void registerRenderers(Class<? extends LivingEntity> entityClass, EntityRenderer entityRenderer, RegistrationHelper registrationHelper);

	/**
	 * A delegate object used to help register feature renderers for an entity renderer.
	 *
	 * <p>This is not meant for implementation by users of the API.
	 */
	interface RegistrationHelper {
		/**
		 * Adds a feature renderer to the entity renderer.
		 *
		 * @param featureRenderer the feature renderer
		 * @param <T>             the type of entity
		 */
		//<T extends LivingEntity> void register(FeatureRenderer<T> featureRenderer);
	}
}
