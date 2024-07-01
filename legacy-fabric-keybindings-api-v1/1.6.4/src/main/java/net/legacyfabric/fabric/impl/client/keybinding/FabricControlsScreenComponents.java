package net.legacyfabric.fabric.impl.client.keybinding;

import net.legacyfabric.fabric.api.util.Identifier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.function.Consumer;

public class FabricControlsScreenComponents {
	private static final Identifier BUTTON_TEX = new Identifier("legacy-fabric", "textures/gui/creative_buttons.png");
	public static final int AMOUNT_PER_PAGE = 14;

	public static class ControlsButtonWidget extends ButtonWidget {
		ControlsScreenExtensions extensions;
		ControlsOptionsScreen gui;
		Type type;

		public ControlsButtonWidget(int x, int y, Type type, ControlsScreenExtensions extensions) {
			super(1000 + type.ordinal(), x, y, 20, 20, "");
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
		public boolean isMouseOver(MinecraftClient client, int mouseX, int mouseY) {
			return super.isMouseOver(client, mouseX, mouseY);
		}

		@Override
		public void mouseDragged(MinecraftClient client, int mouseX, int mouseY) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.visible = extensions.fabric_isButtonVisible(type);
			this.active = extensions.fabric_isButtonEnabled(type);

//			if (this.visible) {
//				int u = active && this.isHovered() ? 22 : 0;
//				int v = active ? 0 : 10;
//
//				MinecraftClient minecraftClient = MinecraftClient.getInstance();
////				minecraftClient.getTextureManager().bindTexture(new net.minecraft.util.Identifier(BUTTON_TEX.toString()));
//
//				GL11.glDisable(GL11.GL_LIGHTING);
//				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//				this.drawTexture(this.x, this.y, u + (type == Type.NEXT ? 11 : 0), v, 11, 10);
//
////				if (this.hovered) {
////					int pageCount = (int) Math.ceil((ItemGroup.itemGroups.length - AMOUNT_PER_PAGE) / 9D);
////					gui.method_1128(I18n.translate("fabric.gui.creativeTabPage", extensions.fabric_currentPage() + 1, pageCount), mouseX, mouseY);
////				}
//			}
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
