/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Formatting;

public class DynamicControlsOptionsScreen extends Screen {
	private Screen parent;
	protected String title = "Controls";
	private GameOptions options;
	private OptimizedControlsListWidget listWidget;

	public DynamicControlsOptionsScreen(Screen parent, GameOptions options) {
		this.parent = parent;
		this.options = options;
	}

	@Override
	public void init() {
		this.buttons.add(new ButtonWidget(200, this.width / 2 - 100, this.height / 6 + 168, I18n.method_5933("gui.done")));
		this.title = I18n.method_5933("controls.title");
		this.listWidget = new OptimizedControlsListWidget();
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		if (button.active) {
			if (button.id == 200) {
				this.client.setScreen(this.parent);
			} else {
				this.listWidget.buttonClicked(button);
			}
		}
	}

	@Override
	protected void keyPressed(char id, int code) {
		if (!this.listWidget.keyPressed(code)) {
			super.keyPressed(id, code);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		this.listWidget.mouseClicked(mouseX, mouseY, button);

		if (!this.listWidget.keyPressed(-100 + button)) {
			super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void handleMouse() {
		super.handleMouse();

		if (!this.client.options.touchscreen) {
			this.listWidget.mouseScrolled(Mouse.getEventDWheel());
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		this.listWidget.render(mouseX, mouseY, tickDelta);
		this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 20, 16777215);

		super.render(mouseX, mouseY, tickDelta);
	}

	public boolean keybindingConflict(int id) {
		int code = this.options.allKeys[id].code;

		for (int i = 0; i < this.options.allKeys.length; i++) {
			KeyBinding key = this.options.allKeys[i];

			if (i != id && key.code == code) return true;
		}

		return false;
	}

	public class OptimizedControlsListWidget extends OptimizedListWidget {
		private int selected = -1;
		private boolean initialClickDone = false;
		public OptimizedControlsListWidget() {
			super(DynamicControlsOptionsScreen.this.client, DynamicControlsOptionsScreen.this.width, DynamicControlsOptionsScreen.this.height, 32, DynamicControlsOptionsScreen.this.height - 65 + 4, 18);
		}

		@Override
		protected int getEntryCount() {
			return DynamicControlsOptionsScreen.this.options.allKeys.length;
		}

		@Override
		protected void method_1057(int i, boolean bl) {
			if (this.selected != i) {
				this.selected = i;
				this.initialClickDone = false;
			}
		}

		@Override
		protected boolean isEntrySelected(int index) {
			return index == selected;
		}

		@Override
		protected void renderBackground() {
			DynamicControlsOptionsScreen.this.renderBackground();
		}

		@Override
		protected void method_1055(int i, int j, int k, int l, Tessellator tessellator) {
			DynamicControlsOptionsScreen.this.drawWithShadow(
					DynamicControlsOptionsScreen.this.textRenderer,
					DynamicControlsOptionsScreen.this.options.getLanguageFromId(i),
					DynamicControlsOptionsScreen.this.width / 4, k + 1, 16777215
			);

			String keyString = DynamicControlsOptionsScreen.this.options.method_874(i);

			if (i == selected) {
				keyString = Formatting.WHITE + "> " + Formatting.YELLOW + "??? " + Formatting.WHITE + "<";
			} else if (DynamicControlsOptionsScreen.this.keybindingConflict(i)) {
				keyString = Formatting.RED + keyString;
			}

			DynamicControlsOptionsScreen.this.drawWithShadow(
					DynamicControlsOptionsScreen.this.textRenderer,
					keyString,
					(DynamicControlsOptionsScreen.this.width / 3) * 2, k + 1, 16777215
			);
		}

		protected boolean keyPressed(int code) {
			if (selected >= 0) {
				if (!this.initialClickDone) {
					this.initialClickDone = true;
				} else {
					DynamicControlsOptionsScreen.this.options.method_867(this.selected, code);
					this.selected = -1;
					KeyBinding.updateKeysByCode();
				}

				return true;
			}

			return false;
		}
	}
}
