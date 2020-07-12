/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.impl.client.keybinding;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.options.KeyBinding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class KeyBindingRegistryImpl implements KeyBindingRegistry {
	public static final KeyBindingRegistryImpl INSTANCE = new KeyBindingRegistryImpl();
	private static final Logger LOGGER = LogManager.getLogger();

	private Set<String> categorySet;
	private final List<FabricKeyBinding> fabricKeyBindingList;

	public KeyBindingRegistryImpl() {
		fabricKeyBindingList = new ArrayList<>();
	}

	private Set<String> getCategorySet() {
		if (categorySet == null) {
			categorySet = KeyBinding.getCategories();
			if (categorySet == null) {
				throw new RuntimeException("Cached key binding category set missing!");
			}
		}

		return categorySet;
	}

	private boolean hasCategory(String categoryName) {
		return this.getCategorySet().contains(categoryName);
	}

	@Override
	public boolean addCategory(String categoryName) {
		Set<String> map = this.getCategorySet();
		if (map.contains(categoryName)) {
			return false;
		}
		map.add(categoryName);
		return true;
	}

	@Override
	public boolean register(FabricKeyBinding binding) {
		for (KeyBinding exBinding : fabricKeyBindingList) {
			if (exBinding == binding) {
				return false;
			}
			else if(binding.hashCode() == exBinding.hashCode()){
				return false;
			}
		}

		if (!hasCategory(binding.getCategory())) {
			LOGGER.warn("Tried to register key binding with unregistered category '" + binding.getCategory() + "' - please use addCategory to ensure intended category ordering!");
			addCategory(binding.getCategory());
		}

		fabricKeyBindingList.add(binding);
		return true;
	}

	public KeyBinding[] process(KeyBinding[] keysAll) {
		List<KeyBinding> newKeysAll = new ArrayList<>();

		for (KeyBinding binding : keysAll) {
			if (!(binding instanceof FabricKeyBinding)) {
				newKeysAll.add(binding);
			}
		}

		newKeysAll.addAll(fabricKeyBindingList);
		return newKeysAll.toArray(new KeyBinding[0]);
	}
}
