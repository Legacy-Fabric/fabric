package net.fabricmc.fabric.impl.client.keybinding;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.util.Identifier;
import org.lwjgl.input.Keyboard;

public class TestMod implements ModInitializer {
	public static final FabricKeyBinding FOO = FabricKeyBinding.Builder.create(new Identifier("foo:bar"), Keyboard.KEY_GRAVE, "key.categories.movement").build();

	@Override
	public void onInitialize() {
		KeyBindingRegistry.INSTANCE.register(FOO);
		ClientTickCallback.EVENT.register((client)->{
			if(FOO.isPressed()){
				System.out.println("KEY PRESSED!!!");
			}
		});
	}
}
