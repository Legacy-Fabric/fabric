package net.legacyfabric.fabric.mixin.item.group.client;

import net.legacyfabric.fabric.impl.item.group.ScreenAccessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin implements ScreenAccessor {
	@Shadow
	protected abstract void method_1128(String par1, int par2, int par3);

	@Override
	public void callRenderTooltip(String text, int x, int y) {
		this.method_1128(text, x, y);
	}
}
