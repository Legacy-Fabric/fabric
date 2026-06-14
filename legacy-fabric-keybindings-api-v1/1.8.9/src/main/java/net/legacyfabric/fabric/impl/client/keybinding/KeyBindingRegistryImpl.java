/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.client.keybinding;

import java.util.ArrayList;
import java.util.List;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.keybinds.api.KeybindEvents;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;

public final class KeyBindingRegistryImpl implements ClientModInitializer {
	private static final List<KeyBinding> moddedKeyBindings = new ArrayList<>();
	private static boolean processed = false;

	public static KeyBinding registerKeyBinding(KeyBinding binding) {
		for (KeyBinding existingKeyBindings : moddedKeyBindings) {
			if (existingKeyBindings == binding) {
				throw new RuntimeException("Attempted to register same key binding twice " + binding.getName() + "!");
			} else if (existingKeyBindings.getName().equals(binding.getName())) {
				throw new RuntimeException("Attempted to register two key bindings with equal ID: " + binding.getName() + "!");
			}
		}

		moddedKeyBindings.add(binding);

		// In 1.7.10 Game Options are loaded before any client entrypoint, so we need to reload when a new keybinding is registered.
		if (processed) reloadGameOptions(binding);

		return binding;
	}

	/**
	 * Update keybinding list and reload game options file.
	 */
	private static void reloadGameOptions(KeyBinding keyBinding) {
		KeybindRegistry.register(keyBinding);
		final GameOptions options = MinecraftInstance.get().options;

		if (options != null) {
			options.load();
		}
	}

	@Override
	public void initClient() {
		KeybindEvents.REGISTER_KEYBINDS.register(() -> {
			moddedKeyBindings.forEach(KeybindRegistry::register);
			processed = true;
		});
	}
}
