package net.legacyfabric.fabric.mixin.fix.bugs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@ModifyArg(method = "method_979", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;method_956(Ljava/lang/String;III)I"))
	private String fixFabricBranding(String string) {
		if (string.contains("Minecraft")) {
			string = string.replace(" (", "/fabric (");
		}

		return string;
	}
}
