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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.item.ItemModelShaper;
import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.resource.loader.ItemModelRegistryImpl;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ItemModelRegistryImpl.Registrar {
	@Shadow
	protected abstract void registerModel(Item item, int metadata, String id);

	@Shadow
	protected abstract void registerModel(Item item, String id);

	@Shadow
	protected abstract void registerModel(Block block, String id);

	@Shadow
	protected abstract void registerModel(Block block, int metadata, String id);

	@Shadow
	@Final
	private ItemModelShaper modelShaper;

	@Override
	public void fabric_register() {
		ItemModelRegistryImpl.ITEMS_WITHOUT_META.forEach(pair -> {
			this.registerModel(pair.getObject(), pair.getModelPath());
		});
		ItemModelRegistryImpl.BLOCKS_WITHOUT_META.forEach(pair -> {
			this.registerModel(pair.getObject(), pair.getModelPath());
		});
		ItemModelRegistryImpl.ITEMS_WITH_META.forEach(triad -> {
			this.registerModel(triad.getObject(), triad.getMetadata(), triad.getModelPath());
		});
		ItemModelRegistryImpl.BLOCKS_WITH_META.forEach(triad -> {
			this.registerModel(triad.getObject(), triad.getMetadata(), triad.getModelPath());
		});
	}

	@Inject(method = "registerModel(Lnet/minecraft/item/Item;ILjava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void lfapi$removeModel(Item item, int metadata, String key, CallbackInfo ci) {
		if (key == null) {
			this.modelShaper.register(item, metadata, null);
			ci.cancel();
		}
	}
}
