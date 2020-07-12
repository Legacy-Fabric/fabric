package net.fabricmc.fabric.api.client.keybinding;

public interface KeyBindingRegistry {
	boolean addCategory(String categoryName);

	boolean register(FabricKeyBinding binding);
}
