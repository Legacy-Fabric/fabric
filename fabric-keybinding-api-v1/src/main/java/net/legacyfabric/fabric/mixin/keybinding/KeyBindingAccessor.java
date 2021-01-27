package net.legacyfabric.fabric.mixin.keybinding;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.options.KeyBinding;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
	@Accessor
	static Map<String, Integer> getCATEGORIES() {
		throw new UnsupportedOperationException();
	}
}
