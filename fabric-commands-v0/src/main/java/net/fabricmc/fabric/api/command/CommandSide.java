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

package net.fabricmc.fabric.api.command;

import com.google.common.annotations.Beta;

public enum CommandSide {
	/**
	 * Not Implemented Yet!
	 */
	@Beta
	CLIENT(false, false),
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
}
