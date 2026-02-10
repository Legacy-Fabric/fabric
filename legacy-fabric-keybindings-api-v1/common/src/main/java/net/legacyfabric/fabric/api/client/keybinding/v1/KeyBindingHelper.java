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

package net.legacyfabric.fabric.api.client.keybinding.v1;

import net.minecraft.client.options.KeyBinding;

import net.legacyfabric.fabric.impl.client.keybinding.KeyBindingRegistryImpl;

/**
 * Helper for registering key bindings.
 *
 * <p>Helper class for {@link KeyBinding} for use by Fabric mods.</p>
 *
 * <pre><code>
 * KeyBinding left = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.example.left", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.category.example"));
 * KeyBinding right = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.example.right", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.category.example"));
 * </code></pre>
 */
public final class KeyBindingHelper {
	private KeyBindingHelper() {
	}

	/**
	 * Registers the keybinding and add the keybinding category if required.
	 *
	 * @param keyBinding the keybinding
	 * @return the keybinding itself
	 */
	public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
		return KeyBindingRegistryImpl.registerKeyBinding(keyBinding);
	}
}
