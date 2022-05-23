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

package net.legacyfabric.fabric.impl.client.command;

import net.legacyfabric.fabric.api.client.command.FabricAbstractClientCommand;
import net.legacyfabric.fabric.api.client.command.FabricClientCommandManager;
import net.legacyfabric.fabric.api.client.command.FabricClientCommandSource;

import net.minecraft.command.CommandSource;

public class ClientSetPrefixCommand extends FabricAbstractClientCommand {
	@Override
	public void execute(FabricClientCommandSource source, String[] args) {
		FabricClientCommandManager.INSTANCE.PREFIX = args[0];
	}

	@Override
	public String getCommandName() {
		return "prefix";
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return "legacyfabric.api.prefixcommand";
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
