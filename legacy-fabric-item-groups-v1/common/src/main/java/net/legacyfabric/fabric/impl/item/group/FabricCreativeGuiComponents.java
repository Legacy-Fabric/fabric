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

package net.legacyfabric.fabric.impl.item.group;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.VersionUtils;

public class FabricCreativeGuiComponents {
	private static final Identifier BUTTON_TEX = new Identifier("legacy-fabric", "textures/gui/creative_buttons.png");
	public static final Set<ItemGroup> COMMON_GROUPS = new HashSet<>();

	public static final boolean hasHotBar = VersionUtils.matches(">=1.12-alpha.17.06.a <=1.13.2");

	public static ItemGroupCreator ITEM_GROUP_CREATOR;

	static {
		COMMON_GROUPS.add(ItemGroup.SEARCH);
		COMMON_GROUPS.add(ItemGroup.INVENTORY);

		if (hasHotBar) {
			for (ItemGroup itemGroup : ItemGroup.itemGroups) {
				if (Objects.equals(((ItemGroupExtensions) itemGroup).getIdentifier(), "hotbar")) {
					COMMON_GROUPS.add(itemGroup);
					break;
				}
			}
		}
	}

	public static class ItemGroupButtonWidget extends ButtonWidget {
		CreativeGuiExtensions extensions;
		CreativeInventoryScreen gui;
		Type type;

		public ItemGroupButtonWidget(int x, int y, Type type, CreativeGuiExtensions extensions) {
			super(1000 + type.ordinal(), x, y, 11, 10, "");
			this.extensions = extensions;
			this.type = type;
			this.gui = (CreativeInventoryScreen) extensions;
		}

		public void click() {
			if (this.active) {
				this.type.clickConsumer.accept(this.extensions);
			}
		}

		@Override
		public boolean isMouseOver(MinecraftClient client, int mouseX, int mouseY) {
			return super.isMouseOver(client, mouseX, mouseY);
		}

		@Override
		public void mouseDragged(MinecraftClient client, int mouseX, int mouseY) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.visible = extensions.fabric_isButtonVisible(type);
			this.active = extensions.fabric_isButtonEnabled(type);

			if (this.visible) {
				int u = active && this.isHovered() ? 22 : 0;
				int v = active ? 0 : 10;

				MinecraftClient minecraftClient = MinecraftClient.getInstance();
				minecraftClient.getTextureManager().bindTexture(new net.minecraft.util.Identifier(BUTTON_TEX.toString()));

				try { // 1.8+
					GlStateManager.disableLighting();
					GlStateManager.color(1F, 1F, 1F, 1F);
				} catch (NoClassDefFoundError e) { // 1.7.10-
					GL11.glDisable(2896);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}

				this.drawTexture(this.x, this.y, u + (type == Type.NEXT ? 11 : 0), v, 11, 10);

				if (this.hovered) {
					int pageCount = (int) Math.ceil((ItemGroup.itemGroups.length - COMMON_GROUPS.size()) / 9D);
					((ScreenAccessor) gui).callRenderTooltip(I18n.translate("fabric.gui.creativeTabPage", extensions.fabric_currentPage() + 1, pageCount), mouseX, mouseY);
				}
			}
		}
	}

	public enum Type {
		NEXT(">", CreativeGuiExtensions::fabric_nextPage),
		PREVIOUS("<", CreativeGuiExtensions::fabric_previousPage);

		final String text;
		final Consumer<CreativeGuiExtensions> clickConsumer;

		Type(String text, Consumer<CreativeGuiExtensions> clickConsumer) {
			this.text = text;
			this.clickConsumer = clickConsumer;
		}
	}

	public interface ItemGroupCreator {
		ItemGroup create(int index, String id, Supplier<ItemStack> itemSupplier, BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay);
	}
}
