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

package net.legacyfabric.fabric.api.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Specifies the side(s) on which the command should be registered.
 */
public enum CommandSide {
	INTEGRATED(false, true),
	DEDICATED(true, false),
	COMMON(true, true);

	private final boolean dedicated;
	private final boolean integrated;

	CommandSide(boolean dedicated, boolean integrated) {
		this.dedicated = dedicated;
		this.integrated = integrated;
	}

	public boolean isDedicated() {
		return this.dedicated;
	}

	public boolean isIntegrated() {
		return this.integrated;
	}

	public static CommandSide getServerSide() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? INTEGRATED : DEDICATED;
	}
}
