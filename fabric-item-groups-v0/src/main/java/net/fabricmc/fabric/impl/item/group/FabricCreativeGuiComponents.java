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

package net.fabricmc.fabric.impl.item.group;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.mixin.item.group.client.MixinScreen;

public class FabricCreativeGuiComponents {
	private static final Identifier BUTTON_TEX = new Identifier("fabric", "textures/gui/creative_buttons.png");
	public static final Set<ItemGroup> COMMON_GROUPS = new HashSet<>();

	static {
		COMMON_GROUPS.add(ItemGroup.SEARCH);
		COMMON_GROUPS.add(ItemGroup.INVENTORY);
	}

	public static class ItemGroupButtonWidget extends ButtonWidget {
		CreativeInventoryScreenExtensions extensions;
		CreativeInventoryScreen gui;
		public Type type;

		public ItemGroupButtonWidget(int id, int x, int y, Type type, CreativeInventoryScreenExtensions extensions) {
			super(id, x, y, 11, 10, type.text);
			this.extensions = extensions;
			this.type = type;
			this.gui = (CreativeInventoryScreen) extensions;
		}

		@Override
		public void render(MinecraftClient client, int mouseX, int mouseY) {
			this.focused = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.visible = extensions.fabric_isButtonVisible(type);
			this.active = extensions.fabric_isButtonEnabled(type);

			if (this.visible) {
				int u = active && this.isFocused() ? 22 : 0;
				int v = active ? 0 : 10;

				MinecraftClient minecraftClient = MinecraftClient.getInstance();
				minecraftClient.getTextureManager().bindTexture(BUTTON_TEX);
				GlStateManager.disableLighting();
				GlStateManager.color4f(1F, 1F, 1F, 1F);
				this.drawTexture(this.x, this.y, u + (type == Type.NEXT ? 11 : 0), v, 11, 10);

				if (this.focused) {
					((MixinScreen) gui).invokeRenderTooltip(new TranslatableText("fabric.gui.creativeTabPage", extensions.fabric_currentPage() + 1, ((ItemGroup.itemGroups.length - 12) / 9) + 2).asString(), mouseX, mouseY);
				}
			}
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			super.mouseReleased(mouseX, mouseY);
		}
	}

	public enum Type {
		NEXT(">", CreativeInventoryScreenExtensions::fabric_nextPage),
		PREVIOUS("<", CreativeInventoryScreenExtensions::fabric_previousPage);

		String text;
		public Consumer<CreativeInventoryScreenExtensions> clickConsumer;

		Type(String text, Consumer<CreativeInventoryScreenExtensions> clickConsumer) {
			this.text = text;
			this.clickConsumer = clickConsumer;
		}
	}
}
