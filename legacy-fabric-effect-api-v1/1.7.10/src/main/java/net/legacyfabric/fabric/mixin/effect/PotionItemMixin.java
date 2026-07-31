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

package net.legacyfabric.fabric.mixin.effect;

import java.util.List;
import java.util.Map;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;

import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {
	@Shadow
	@Final
	private static Map<List<StatusEffectInstance>, Integer> ITEM_STACKS;

	@Shadow
	private Map<Integer, List<StatusEffectInstance>> potionEffectsByMetadataCache;

	@Unique
	private static int lf$potionItemCount;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void api$trackPotionItems(CallbackInfo ci) {
		SyncedRegistries.registerFixer(VanillaRegistryKeys.STATUS_EFFECT,
				NamespacedIdentifiers.from("potion/" + (lf$potionItemCount++) + "/potion_effect_by_metadata_cache"),
				() -> potionEffectsByMetadataCache.clear());
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerStackFixer(CallbackInfo ci) {
		SyncedRegistries.registerFixer(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("potion/item_stacks"), () -> ITEM_STACKS.clear());
	}
}
