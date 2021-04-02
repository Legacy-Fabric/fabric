package net.legacyfabric.fabric.test.client;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.options.KeyBinding;

import net.fabricmc.api.ClientModInitializer;

public class KeybindingTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("api.keybinding.testTranslationKey", Keyboard.KEY_F, "key.categories.misc"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (keyBinding.wasPressed()) {
				System.out.printf("The key %s was pressed%n", Keyboard.getKeyName(keyBinding.getCode()));
			}
		});
	}
}
