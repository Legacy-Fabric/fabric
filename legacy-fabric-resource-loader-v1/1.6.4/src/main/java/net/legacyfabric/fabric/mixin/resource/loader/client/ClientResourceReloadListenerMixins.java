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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.FoliageColorResourceReloadListener;
import net.minecraft.resource.GrassColorResourceReloadListener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.resource.ResourceReloadListenerKeys;
import net.legacyfabric.fabric.api.util.Identifier;

@Mixin({
		SoundSystem.class, GameRenderer.class, LanguageManager.class, GrassColorResourceReloadListener.class, FoliageColorResourceReloadListener.class, TextureManager.class,
		WorldRenderer.class, ItemRenderer.class, TextRenderer.class
})
@Environment(EnvType.CLIENT)
public abstract class ClientResourceReloadListenerMixins implements IdentifiableResourceReloadListener {
	private Collection<Identifier> fabric_idDeps;
	private Identifier fabric_id;

	@SuppressWarnings("ConstantConditions")
	@Override
	public Collection<Identifier> getFabricDependencies() {
		if (this.fabric_idDeps == null) {
			Object self = this;

			if (self instanceof WorldRenderer) {
				this.fabric_idDeps = Collections.singletonList(ResourceReloadListenerKeys.TEXTURES);
			} else if (self instanceof ItemRenderer) {
				this.fabric_idDeps = Collections.singletonList(ResourceReloadListenerKeys.MODELS);
			} else {
				this.fabric_idDeps = Collections.emptyList();
			}
		}

		return this.fabric_idDeps;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public Identifier getFabricId() {
		if (this.fabric_id == null) {
			Object self = this;

			if (self instanceof SoundSystem) {
				this.fabric_id = ResourceReloadListenerKeys.SOUNDS;
			} else if (self instanceof TextRenderer) {
				this.fabric_id = ResourceReloadListenerKeys.FONTS;
			} else if (self instanceof LanguageManager) {
				this.fabric_id = ResourceReloadListenerKeys.LANGUAGES;
			} else if (self instanceof TextureManager) {
				this.fabric_id = ResourceReloadListenerKeys.TEXTURES;
			} else {
				MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
				this.fabric_id = new Identifier("minecraft", "private/" + resolver.mapClassName("intermediary", self.getClass().getName()).toLowerCase(Locale.ROOT));
			}
		}

		return this.fabric_id;
	}
}
