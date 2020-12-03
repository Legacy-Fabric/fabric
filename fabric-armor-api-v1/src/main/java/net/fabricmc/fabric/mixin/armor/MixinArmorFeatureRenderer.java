/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.mixin.armor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.armor.FabricArmorItem;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public class MixinArmorFeatureRenderer {
	@Redirect(at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"), method = "method_4092")
	public String loadArmorTextures(String format, Object[] args, ArmorItem armorItem, boolean bl, String string) {
		if (armorItem instanceof FabricArmorItem) {
			return String.format("%s:textures/models/armor/%s_layer_%d.png", Item.REGISTRY.getIdentifier(armorItem).getNamespace(), ((FabricArmorItem) armorItem).getArmorMaterial().getName(), bl ? 2 : 1);
		}

		return String.format("textures/models/armor/%s_layer_%d%s.png", armorItem.getMaterial().getName(), bl ? 2 : 1, string == null ? "" : String.format("_%s", string));
	}
}
