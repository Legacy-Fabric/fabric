package net.legacyfabric.fabric.mixin.client.keybinding;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

	@Shadow
	@Final
	private static Map<String, Integer> field_15867;
	private static int lf$category_count = 7;
	@Inject(method = "<init>", at = @At("RETURN"))
	private void registerCategory(String string, int i, String category, CallbackInfo ci) {
		if (!field_15867.containsKey(category)) {
			while (field_15867.containsValue(lf$category_count)) lf$category_count++;
			field_15867.put(category, lf$category_count);
		}
	}
}
