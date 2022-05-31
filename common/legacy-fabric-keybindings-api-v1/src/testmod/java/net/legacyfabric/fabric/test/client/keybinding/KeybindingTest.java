/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.test.client.keybinding;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.options.KeyBinding;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class KeybindingTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("api.keybinding.testTranslationKey", Keyboard.KEY_F, "key.categories.misc"));
		//		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		//			if (keyBinding.wasPressed()) {
		//				System.out.printf("The key %s was pressed%n", Keyboard.getKeyName(keyBinding.getCode()));
		//			}
		//		});
	}
}
