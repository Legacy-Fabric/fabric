/*
 * Copyright (c) 2021 Legacy Rewoven
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

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.command.AbstractCommand;

import io.github.legacyrewoven.api.command.CommandSide;
import io.github.legacyrewoven.api.registry.CommandRegistry;

public class CommandRegistryImpl implements CommandRegistry {
	public static final CommandRegistryImpl INSTANCE = new CommandRegistryImpl();
	private static final Map<AbstractCommand, CommandSide> FABRIC_COMMANDS = Maps.newHashMap();

	@Override
	public void register(AbstractCommand command, CommandSide side) {
		FABRIC_COMMANDS.put(command, side);
	}

	public static Map<AbstractCommand, CommandSide> getCommandMap() {
		return Collections.unmodifiableMap(FABRIC_COMMANDS);
	}
}
