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
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class OptimizedListWidget {
	private final MinecraftClient client;
	private int field_1247;
	private int height;
	protected int yStart;
	protected int yEnd;
	private int xEnd;
	private int field_1250;
	protected final int entryHeight;
	private int homeButtonId;
	private int endButtonId;
	protected int lastMouseX;
	protected int lastMouseY;
	private float field_1253 = -2.0F;
	private float scrollSpeed;
	private float scrollAmount;
	private int selectedEntry = -1;
	private long time;
	private boolean field_1258 = true;
	private boolean renderHeader;
	private int headerHeight;

	public OptimizedListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
		this.client = minecraftClient;
		this.field_1247 = i;
		this.height = j;
		this.yStart = k;
		this.yEnd = l;
		this.entryHeight = m;
		this.field_1250 = 0;
		this.xEnd = i;
	}

	public void updateBounds(int right, int height, int top, int bottom) {
		this.field_1247 = right;
		this.height = height;
		this.yStart = top;
		this.yEnd = bottom;
		this.field_1250 = 0;
		this.xEnd = right;
	}

	public void setRenderSelection(boolean renderSelection) {
		this.field_1258 = renderSelection;
	}

	protected void setHeader(boolean renderHeader, int headerHeight) {
		this.renderHeader = renderHeader;
		this.headerHeight = headerHeight;

		if (!renderHeader) {
			this.headerHeight = 0;
		}
	}

	protected abstract int getEntryCount();

	protected abstract void method_1057(int i, boolean bl);

	protected abstract boolean isEntrySelected(int index);

	protected int getMaxPosition() {
		return this.getEntryCount() * this.entryHeight + this.headerHeight;
	}

	protected abstract void renderBackground();

	protected abstract void method_1055(int i, int j, int k, int l, Tessellator tessellator);

	protected void renderHeader(int x, int y, Tessellator tessellator) {
	}

	protected void clickedHeader(int mouseX, int mouseY) {
	}

	protected void renderDecorations(int mouseX, int mouseY) {
	}

	public int getEntryAt(int x, int y) {
		int var3 = this.field_1247 / 2 - 110;
		int var4 = this.field_1247 / 2 + 110;
		int var5 = y - this.yStart - this.headerHeight + (int) this.scrollAmount - 4;
		int var6 = var5 / this.entryHeight;
		return x >= var3 && x <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getEntryCount() ? var6 : -1;
	}

	public void setButtonIds(int homeButtonId, int endButtonId) {
		this.homeButtonId = homeButtonId;
		this.endButtonId = endButtonId;
	}

	private void capYPosition() {
		int var1 = this.getMaxScroll();

		if (var1 < 0) {
			var1 /= 2;
		}

		if (this.scrollAmount < 0.0F) {
			this.scrollAmount = 0.0F;
		}

		if (this.scrollAmount > (float) var1) {
			this.scrollAmount = (float) var1;
		}
	}

	public int getMaxScroll() {
		return this.getMaxPosition() - (this.yEnd - this.yStart - 4);
	}

	public void scroll(int amount) {
		this.scrollAmount += (float) amount;
		this.capYPosition();
		this.field_1253 = -2.0F;
	}

	public void buttonClicked(ButtonWidget button) {
		if (button.active) {
			if (button.id == this.homeButtonId) {
				this.scrollAmount -= (float) (this.entryHeight * 2 / 3);
				this.field_1253 = -2.0F;
				this.capYPosition();
			} else if (button.id == this.endButtonId) {
				this.scrollAmount += (float) (this.entryHeight * 2 / 3);
				this.field_1253 = -2.0F;
				this.capYPosition();
			}
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (button == 0) {
			int var4 = this.getEntryCount();
			int var5 = this.getScrollbarPosition();
			int var6 = var5 + 6;

			if (this.field_1253 == -1.0F) {
				boolean var16 = true;

				if (mouseY >= this.yStart && mouseY <= this.yEnd) {
					int var8 = this.field_1247 / 2 - 110;
					int var9 = this.field_1247 / 2 + 110;
					int var10 = mouseY - this.yStart - this.headerHeight + (int) this.scrollAmount - 4;
					int var11 = var10 / this.entryHeight;

					if (mouseX >= var8 && mouseX <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
						boolean var12 = var11 == this.selectedEntry && MinecraftClient.getTime() - this.time < 250L;
						this.method_1057(var11, var12);
						this.selectedEntry = var11;
						this.time = MinecraftClient.getTime();
					} else if (mouseX >= var8 && mouseX <= var9 && var10 < 0) {
						this.clickedHeader(mouseX - var8, mouseY - this.yStart + (int) this.scrollAmount - 4);
						var16 = false;
					}

					if (mouseX >= var5 && mouseX <= var6) {
						this.scrollSpeed = -1.0F;
						int var23 = this.getMaxScroll();

						if (var23 < 1) {
							var23 = 1;
						}

						int var13 = (int) ((float) ((this.yEnd - this.yStart) * (this.yEnd - this.yStart)) / (float) this.getMaxPosition());

						if (var13 < 32) {
							var13 = 32;
						}

						if (var13 > this.yEnd - this.yStart - 8) {
							var13 = this.yEnd - this.yStart - 8;
						}

						this.scrollSpeed /= (float) (this.yEnd - this.yStart - var13) / (float) var23;
					} else {
						this.scrollSpeed = 1.0F;
					}

					if (var16) {
						this.field_1253 = (float) mouseY;
					} else {
						this.field_1253 = -2.0F;
					}
				} else {
					this.field_1253 = -2.0F;
				}
			} else if (this.field_1253 >= 0.0F) {
				this.scrollAmount -= ((float) mouseY - this.field_1253) * this.scrollSpeed;
				this.field_1253 = (float) mouseY;
			}
		}
	}

	public void mouseScrolled(int var7) {
		if (var7 != 0) {
			if (var7 > 0) {
				var7 = -1;
			} else if (var7 < 0) {
				var7 = 1;
			}

			this.scrollAmount += (float) (var7 * this.entryHeight / 2);
		}
	}

	public void render(int mouseX, int mouseY, float tickDelta) {
		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
		this.renderBackground();
		int var4 = this.getEntryCount();
		int var5 = this.getScrollbarPosition();
		int var6 = var5 + 6;

		if (!Mouse.isButtonDown(0)) {
			this.field_1253 = -1.0F;
		}

		this.capYPosition();
		GL11.glDisable(2896);
		GL11.glDisable(2912);
		Tessellator var17 = Tessellator.INSTANCE;
		this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var18 = 32.0F;
		var17.begin();
		var17.color(2105376);
		var17.vertex(
				(double) this.field_1250, (double) this.yEnd, 0.0, (double) ((float) this.field_1250 / var18), (double) ((float) (this.yEnd + (int) this.scrollAmount) / var18)
		);
		var17.vertex((double) this.xEnd, (double) this.yEnd, 0.0, (double) ((float) this.xEnd / var18), (double) ((float) (this.yEnd + (int) this.scrollAmount) / var18));
		var17.vertex((double) this.xEnd, (double) this.yStart, 0.0, (double) ((float) this.xEnd / var18), (double) ((float) (this.yStart + (int) this.scrollAmount) / var18));
		var17.vertex(
				(double) this.field_1250, (double) this.yStart, 0.0, (double) ((float) this.field_1250 / var18), (double) ((float) (this.yStart + (int) this.scrollAmount) / var18)
		);
		var17.end();
		int var19 = this.field_1247 / 2 - 92 - 16;
		int var20 = this.yStart + 4 - (int) this.scrollAmount;

		if (this.renderHeader) {
			this.renderHeader(var19, var20, var17);
		}

		for (int var21 = 0; var21 < var4; ++var21) {
			int var24 = var20 + var21 * this.entryHeight + this.headerHeight;
			int var26 = this.entryHeight - 4;

			if (var24 <= this.yEnd && var24 + var26 >= this.yStart) {
				if (this.field_1258 && this.isEntrySelected(var21)) {
					int var14 = this.field_1247 / 2 - 110;
					int var15 = this.field_1247 / 2 + 110;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(3553);
					var17.begin();
					var17.color(8421504);
					var17.vertex((double) var14, (double) (var24 + var26 + 2), 0.0, 0.0, 1.0);
					var17.vertex((double) var15, (double) (var24 + var26 + 2), 0.0, 1.0, 1.0);
					var17.vertex((double) var15, (double) (var24 - 2), 0.0, 1.0, 0.0);
					var17.vertex((double) var14, (double) (var24 - 2), 0.0, 0.0, 0.0);
					var17.color(0);
					var17.vertex((double) (var14 + 1), (double) (var24 + var26 + 1), 0.0, 0.0, 1.0);
					var17.vertex((double) (var15 - 1), (double) (var24 + var26 + 1), 0.0, 1.0, 1.0);
					var17.vertex((double) (var15 - 1), (double) (var24 - 1), 0.0, 1.0, 0.0);
					var17.vertex((double) (var14 + 1), (double) (var24 - 1), 0.0, 0.0, 0.0);
					var17.end();
					GL11.glEnable(3553);
				}

				this.method_1055(var21, var19, var24, var26, var17);
			}
		}

		GL11.glDisable(2929);
		byte var22 = 4;
		this.renderHoleBackground(0, this.yStart, 255, 255);
		this.renderHoleBackground(this.yEnd, this.height, 255, 255);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3008);
		GL11.glShadeModel(7425);
		GL11.glDisable(3553);
		var17.begin();
		var17.color(0, 0);
		var17.vertex((double) this.field_1250, (double) (this.yStart + var22), 0.0, 0.0, 1.0);
		var17.vertex((double) this.xEnd, (double) (this.yStart + var22), 0.0, 1.0, 1.0);
		var17.color(0, 255);
		var17.vertex((double) this.xEnd, (double) this.yStart, 0.0, 1.0, 0.0);
		var17.vertex((double) this.field_1250, (double) this.yStart, 0.0, 0.0, 0.0);
		var17.end();
		var17.begin();
		var17.color(0, 255);
		var17.vertex((double) this.field_1250, (double) this.yEnd, 0.0, 0.0, 1.0);
		var17.vertex((double) this.xEnd, (double) this.yEnd, 0.0, 1.0, 1.0);
		var17.color(0, 0);
		var17.vertex((double) this.xEnd, (double) (this.yEnd - var22), 0.0, 1.0, 0.0);
		var17.vertex((double) this.field_1250, (double) (this.yEnd - var22), 0.0, 0.0, 0.0);
		var17.end();
		int var25 = this.getMaxScroll();

		if (var25 > 0) {
			int var27 = (this.yEnd - this.yStart) * (this.yEnd - this.yStart) / this.getMaxPosition();

			if (var27 < 32) {
				var27 = 32;
			}

			if (var27 > this.yEnd - this.yStart - 8) {
				var27 = this.yEnd - this.yStart - 8;
			}

			int var28 = (int) this.scrollAmount * (this.yEnd - this.yStart - var27) / var25 + this.yStart;

			if (var28 < this.yStart) {
				var28 = this.yStart;
			}

			var17.begin();
			var17.color(0, 255);
			var17.vertex((double) var5, (double) this.yEnd, 0.0, 0.0, 1.0);
			var17.vertex((double) var6, (double) this.yEnd, 0.0, 1.0, 1.0);
			var17.vertex((double) var6, (double) this.yStart, 0.0, 1.0, 0.0);
			var17.vertex((double) var5, (double) this.yStart, 0.0, 0.0, 0.0);
			var17.end();
			var17.begin();
			var17.color(8421504, 255);
			var17.vertex((double) var5, (double) (var28 + var27), 0.0, 0.0, 1.0);
			var17.vertex((double) var6, (double) (var28 + var27), 0.0, 1.0, 1.0);
			var17.vertex((double) var6, (double) var28, 0.0, 1.0, 0.0);
			var17.vertex((double) var5, (double) var28, 0.0, 0.0, 0.0);
			var17.end();
			var17.begin();
			var17.color(12632256, 255);
			var17.vertex((double) var5, (double) (var28 + var27 - 1), 0.0, 0.0, 1.0);
			var17.vertex((double) (var6 - 1), (double) (var28 + var27 - 1), 0.0, 1.0, 1.0);
			var17.vertex((double) (var6 - 1), (double) var28, 0.0, 1.0, 0.0);
			var17.vertex((double) var5, (double) var28, 0.0, 0.0, 0.0);
			var17.end();
		}

		this.renderDecorations(mouseX, mouseY);
		GL11.glEnable(3553);
		GL11.glShadeModel(7424);
		GL11.glEnable(3008);
		GL11.glDisable(3042);
	}

	protected int getScrollbarPosition() {
		return this.field_1247 / 2 + 124;
	}

	private void renderHoleBackground(int top, int bottom, int topAlpha, int bottomAlpha) {
		Tessellator var5 = Tessellator.INSTANCE;
		this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var6 = 32.0F;
		var5.begin();
		var5.color(4210752, bottomAlpha);
		var5.vertex(0.0, (double) bottom, 0.0, 0.0, (double) ((float) bottom / var6));
		var5.vertex((double) this.field_1247, (double) bottom, 0.0, (double) ((float) this.field_1247 / var6), (double) ((float) bottom / var6));
		var5.color(4210752, topAlpha);
		var5.vertex((double) this.field_1247, (double) top, 0.0, (double) ((float) this.field_1247 / var6), (double) ((float) top / var6));
		var5.vertex(0.0, (double) top, 0.0, 0.0, (double) ((float) top / var6));
		var5.end();
	}
}
