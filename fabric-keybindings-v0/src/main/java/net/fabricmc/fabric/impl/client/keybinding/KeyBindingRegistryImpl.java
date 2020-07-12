package net.fabricmc.fabric.impl.client.keybinding;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;

public class KeyBindingRegistryImpl implements KeyBindingRegistry {
	@Override
	public boolean addCategory(String categoryName) {
		return false;
	}

	@Override
	public boolean register(FabricKeyBinding binding) {
		return false;
	}
}
