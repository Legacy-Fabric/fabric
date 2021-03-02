/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.api.command.v2;

import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandManager;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * An entrypoint and event for registering commands to the {@link CommandManager}.
 */
@FunctionalInterface
public interface CommandRegistrar {
	Event<CommandRegistrar> EVENT = EventFactory.createArrayBacked(CommandRegistrar.class, listeners -> (manager, dedicated) -> {
		for (CommandRegistrar registrar : listeners) {
			registrar.register(manager, dedicated);
		}
	});

	/**
	 * Register your commands here.
	 *
	 * @param manager   The command manager
	 * @param dedicated Whether the mod is running on a dedicated server
	 */
	void register(CommandManager manager, boolean dedicated);
}
