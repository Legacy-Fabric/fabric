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

package net.fabricmc.fabric.api.client.keybinding;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FabricKeyBinding extends KeyBinding {

	private final String translationKey;
	private final String categoryKey;
	private final int keyCode;

	private FabricKeyBinding(Identifier identifier, int keyCode, String categoryKey) {
		super("key." + identifier.getNamespace() + identifier.getPath(), keyCode, categoryKey);
		this.translationKey = "key." + identifier.getNamespace() + identifier.getPath();
		this.categoryKey = categoryKey;
		this.keyCode = keyCode;
	}

	@Override
	public int getDefaultCode() {
		return this.keyCode;
	}

	@Override
	public String getTranslationKey() {
		return this.translationKey;
	}

	@Override
	public String getCategory() {
		return this.categoryKey;
	}

	public static class Builder {
		protected final FabricKeyBinding binding;

		public Builder(FabricKeyBinding binding) {
			this.binding = binding;
		}

		public FabricKeyBinding build() {
			return binding;
		}

		public static Builder create(Identifier id, int code, String category) {
			return new Builder(new FabricKeyBinding(id, code, category));
		}
	}
}
