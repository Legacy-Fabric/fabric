/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package io.github.legacyrewoven.impl.command;

import io.github.legacyrewoven.api.command.v2.CommandRegistrar;
import io.github.legacyrewoven.api.command.v2.lib.sponge.CommandManager;
import io.github.legacyrewoven.api.registry.CommandRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ImplInit implements DedicatedServerModInitializer, ClientModInitializer, CommandRegistrar {
	@Override
	public void register(CommandManager manager, boolean dedicated) {
		CommandRegistrar.EVENT.invoker().register(manager, dedicated);
		InternalObjects.getCommandManager().getCommands().forEach(mapping -> {
			CommandWrapper wrapper = new CommandWrapper(mapping);
			CommandRegistry.INSTANCE.register(wrapper);
		});
	}

	@Override
	public void onInitializeClient() {
		this.register(InternalObjects.getCommandManager(), false);
	}

	@Override
	public void onInitializeServer() {
		this.register(InternalObjects.getCommandManager(), true);
	}
}
