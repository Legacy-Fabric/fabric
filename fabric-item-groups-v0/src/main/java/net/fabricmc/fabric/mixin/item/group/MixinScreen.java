package net.fabricmc.fabric.mixin.item.group;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public interface MixinScreen {
	@Invoker
	void invokeRenderTooltip(String text, int x, int y);
}
