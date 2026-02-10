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

package net.legacyfabric.fabric.impl.client.keybinding;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

import net.legacyfabric.fabric.api.util.Identifier;

public class FabricControlsScreenComponents {
	private static final Identifier BUTTON_TEX = new Identifier("legacy-fabric", "textures/gui/creative_buttons.png");
	public static final int AMOUNT_PER_PAGE = 14;

	public static class ControlsButtonWidget extends ButtonWidget implements ButtonWidgetHelper {
		ControlsScreenExtensions extensions;
		ControlsOptionsScreen gui;
		Type type;

		public ControlsButtonWidget(int x, int y, Type type, ControlsScreenExtensions extensions) {
			super(1000 + type.ordinal(), x, y, 20, 20, type.text);
			this.extensions = extensions;
			this.type = type;
			this.gui = (ControlsOptionsScreen) extensions;
		}

		public void click() {
			if (this.active) {
				this.type.clickConsumer.accept(this.extensions);
			}
		}

		@Override
		public void lf$mouseDragged(int mouseX, int mouseY, BiFunction<Integer, Integer, Void> superMethod) {
			this.lf$setHovered(mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
			this.visible = extensions.fabric_isButtonVisible(type);
			this.active = extensions.fabric_isButtonEnabled(type);
			superMethod.apply(mouseX, mouseY);
		}
	}

	public enum Type {
		NEXT(">", ControlsScreenExtensions::fabric_nextPage),
		PREVIOUS("<", ControlsScreenExtensions::fabric_previousPage);

		final String text;
		final Consumer<ControlsScreenExtensions> clickConsumer;

		Type(String text, Consumer<ControlsScreenExtensions> clickConsumer) {
			this.text = text;
			this.clickConsumer = clickConsumer;
		}
	}
}
