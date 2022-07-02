/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.fix.bugs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@ModifyArg(method = "method_979", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;method_956(Ljava/lang/String;III)I"))
	private String fixFabricBranding(String string) {
		if (string.contains("Minecraft")) {
			string = string.replace(" (", "/fabric (");
		}

		return string;
	}
}
