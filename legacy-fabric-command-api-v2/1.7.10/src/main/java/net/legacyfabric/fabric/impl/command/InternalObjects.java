/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.impl.command;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher.SimpleDispatcher;

public class InternalObjects {
	private static final CommandManagerImpl COMMAND_MANAGER = new CommandManagerImpl(SimpleDispatcher.FIRST_DISAMBIGUATOR);

	public static CommandManagerImpl getCommandManager() {
		return COMMAND_MANAGER;
	}
}
