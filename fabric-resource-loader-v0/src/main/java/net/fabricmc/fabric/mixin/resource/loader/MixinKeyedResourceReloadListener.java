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

package net.fabricmc.fabric.mixin.resource.loader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.class_1254;
import net.minecraft.class_1255;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public class MixinKeyedResourceReloadListener {
	@Mixin({
			       SoundManager.class, GameRenderer.class, LanguageManager.class, class_1255.class, class_1254.class, TextureManager.class,
			       WorldRenderer.class, BlockRenderManager.class, ItemRenderer.class, SpriteAtlasTexture.class, TextRenderer.class
	       })
	public abstract static class Client implements IdentifiableResourceReloadListener {
		private Collection<Identifier> fabric_idDeps;
		private Identifier fabric_id;
		
		@Override
		@SuppressWarnings({"ConstantConditions", "RedundantCast"})
		public Collection<Identifier> getFabricDependencies() {
			if (fabric_idDeps == null) {
				Object self = this;

//				if (self instanceof BakedModelManager || self instanceof WorldRenderer) {
//					fabric_idDeps = Collections.singletonList(ResourceReloadListenerKeys.TEXTURES);
//				} else if (self instanceof ItemRenderer || self instanceof BlockRenderManager) {
//					fabric_idDeps = Collections.singletonList(ResourceReloadListenerKeys.MODELS);
//				} else {
				fabric_idDeps = Collections.emptyList();
//				}
			}
			
			return fabric_idDeps;
		}
		
		@Override
		@SuppressWarnings({"ConstantConditions", "RedundantCast"})
		public Identifier getFabricId() {
			if (fabric_id == null) {
				Object self = this;

//				if (self instanceof SoundLoader) {
//					fabric_id = ResourceReloadListenerKeys.SOUNDS;
//				} else if (self instanceof FontManager) {
//					fabric_id = ResourceReloadListenerKeys.FONTS;
//				} else if (self instanceof BakedModelManager) {
//					fabric_id = ResourceReloadListenerKeys.MODELS;
//				} else if (self instanceof class_1270) {
//					fabric_id = ResourceReloadListenerKeys.LANGUAGES;
//				} else if (self instanceof TextureManager) {
//					fabric_id = ResourceReloadListenerKeys.TEXTURES;
//				} else {
				fabric_id = new Identifier("minecraft", "private/" + self.getClass().getSimpleName().toLowerCase(Locale.ROOT));
//				}
			}
			
			return fabric_id;
		}
	}
}
