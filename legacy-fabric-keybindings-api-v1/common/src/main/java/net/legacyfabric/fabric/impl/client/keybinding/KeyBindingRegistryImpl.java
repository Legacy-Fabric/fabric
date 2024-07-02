/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

public final class KeyBindingRegistryImpl {
	private static final List<KeyBinding> moddedKeyBindings = Lists.newArrayList();
	private static boolean processed = false;

	private KeyBindingRegistryImpl() {
	}

	public static KeyBinding registerKeyBinding(KeyBinding binding) {
		for (KeyBinding existingKeyBindings : moddedKeyBindings) {
			if (existingKeyBindings == binding) {
				throw new RuntimeException("Attempted to register same key binding twice " + binding.translationKey + "!");
			} else if (existingKeyBindings.translationKey.equals(binding.translationKey)) {
				throw new RuntimeException("Attempted to register two key bindings with equal ID: " + binding.translationKey + "!");
			}
		}

		moddedKeyBindings.add(binding);

		// In 1.7.10 Game Options are loaded before any client entrypoint, so we need to reload when a new keybinding is registered.
		if (processed) reloadGameOptions();

		return binding;
	}

	// Processes the keybindings array for our modded ones by
	// first removing existing modded keybindings and re-adding
	// them, we can make sure that there are no duplicates this way.
	public static KeyBinding[] process(KeyBinding[] keysAll) {
		List<KeyBinding> newKeysAll = Lists.newArrayList(keysAll);
		newKeysAll.removeAll(moddedKeyBindings);
		newKeysAll.addAll(moddedKeyBindings);

		processed = true;

		return newKeysAll.toArray(new KeyBinding[0]);
	}

	/**
	 * Update keybinding list and reload game options file.
	 */
	private static void reloadGameOptions() {
		final GameOptions options = MinecraftClient.getInstance().options;

		if (options != null) {
			options.allKeys = process(options.allKeys);
			options.load();
		}
	}
}
