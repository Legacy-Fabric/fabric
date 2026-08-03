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

package net.legacyfabric.fabric.mixin.enchantment;

import net.legacyfabric.fabric.api.enchantment.EnchantmentExtension;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ObjectArrayMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;

import net.legacyfabric.fabric.impl.enchantment.versioned.EnchantmentRegistryImpl;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentExtension {
	@Shadow
	protected String key;

	@Shadow
	@Final
	private static Enchantment[] BY_ID;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void lf$unlockRegistry(CallbackInfo ci) {
		EnchantmentRegistryImpl.unlock();
	}

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", remap = false))
	private static void api$registerRegistry(CallbackInfo ci) {
		EnchantmentRegistryImpl.registerEnchantments();

		SyncedRegistries.registerMapper(VanillaRegistryKeys.ENCHANTMENT, NamespacedIdentifiers.from("enchantment/by_id"), ObjectArrayMapper.of(BY_ID));
	}

	@Inject(
			method = "getTranslationKey",
			at = @At(
					value = "HEAD"
			)
	)
	private void osl$blocks$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = EnchantmentRegistryImpl.getIdentifier((Enchantment) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}
}
