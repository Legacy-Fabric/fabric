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

package net.legacyfabric.fabric.api.client.command;

import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;

public interface FabricClientCommand extends Command {
	@Override
	default void execute(CommandSource source, String[] args) {
		if (source instanceof FabricClientCommandSource) {
			this.execute((FabricClientCommandSource) source, args);
		} else {
			throw new IllegalArgumentException("Command source is not instance of FabricClientCommandSource!");
		}
	}

	void execute(FabricClientCommandSource source, String[] args);
}
