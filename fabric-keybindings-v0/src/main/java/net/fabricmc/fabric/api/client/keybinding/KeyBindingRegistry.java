package net.fabricmc.fabric.api.client.keybinding;

import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;

public interface KeyBindingRegistry {
	KeyBindingRegistry INSTANCE = new KeyBindingRegistryImpl();

	boolean addCategory(String categoryName);

	boolean register(FabricKeyBinding binding);
}
