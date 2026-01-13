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

package net.legacyfabric.fabric.mixin.client.keybinding;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

import net.legacyfabric.fabric.impl.client.keybinding.ButtonWidgetHelper;

@Mixin(ButtonWidget.class)
public abstract class ButtonWidgetMixin implements ButtonWidgetHelper {
	@Shadow
	protected boolean hovered;

	@WrapMethod(method = "method_891")
	private void fabric_mouseDragged(Minecraft client, int mouseX, int mouseY, Operation<Void> original) {
		this.lf$mouseDragged(mouseX, mouseY, (x, y) -> original.call(client, x, y));
	}

	@Override
	public void lf$setHovered(boolean hovered) {
		this.hovered = hovered;
	}
}
