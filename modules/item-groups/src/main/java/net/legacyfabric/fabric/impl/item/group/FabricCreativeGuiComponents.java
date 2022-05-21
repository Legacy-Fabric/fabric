package net.legacyfabric.fabric.impl.item.group;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FabricCreativeGuiComponents {
	private static final Identifier BUTTON_TEX = new Identifier("fabric", "textures/gui/creative_buttons.png");
	public static final Set<ItemGroup> COMMON_GROUPS = new HashSet<>();

	static {
		COMMON_GROUPS.add(ItemGroup.SEARCH);
		COMMON_GROUPS.add(ItemGroup.INVENTORY);
	}

	public static class ItemGroupButtonWidget extends ButtonWidget {
		CreativeGuiExtensions extensions;
		CreativeInventoryScreen gui;
		Type type;

		public ItemGroupButtonWidget(int id, int x, int y, Type type, CreativeGuiExtensions extensions) {
			super(id, x, y, 11, 10, type.text);
			this.extensions = extensions;
			this.type = type;
			this.gui = (CreativeInventoryScreen) extensions;
		}

		@Override
		public void render(MinecraftClient client, int mouseX, int mouseY) {
			super.render(client, mouseX, mouseY);
			fill(x, y, x + 11, y + 10, (type == Type.PREVIOUS ? Color.RED : Color.GREEN).getRGB());
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			super.mouseReleased(mouseX, mouseY);
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
}
