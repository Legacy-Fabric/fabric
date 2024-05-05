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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.ChatMessage;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.PatternMatchingCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class ModCommandElement extends PatternMatchingCommandElement {
	public ModCommandElement(@Nullable ChatMessage key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(PermissibleCommandSource source) {
		return FabricLoader.getInstance().getAllMods().stream().map(container -> container.getMetadata().getId()).collect(Collectors.toSet());
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		Optional<ModContainer> plugin = FabricLoader.getInstance().getModContainer(choice);
		return plugin.orElseThrow(() -> new IllegalArgumentException("Mod " + choice + " was not found"));
	}
}
