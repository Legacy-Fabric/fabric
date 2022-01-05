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

package io.github.legacyrewoven.api.registry;

import io.github.legacyrewoven.api.command.CommandSide;
import io.github.legacyrewoven.impl.command.CommandRegistryImpl;

import net.minecraft.command.AbstractCommand;

/**
 * Base class for registration of commands.
 */
public interface CommandRegistry {
	CommandRegistry INSTANCE = CommandRegistryImpl.INSTANCE;

	void register(AbstractCommand command, CommandSide side);

	default void register(AbstractCommand command) {
		this.register(command, CommandSide.COMMON);
	}
}
