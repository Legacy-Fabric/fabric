package net.me;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.input.Keyboard;

public class Test implements ModInitializer {

	private static FabricKeyBinding keyBinding;

	@Override
	public void onInitialize() {
		keyBinding = FabricKeyBinding.Builder.create(
				new Identifier("tutorial", "spook"),
				Keyboard.KEY_R,
				"Wiki Keybinds"
		).build();
	}
}
