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

package net.legacyfabric.fabric.api.command.v2;

/**
 * Specifies a method to parse {@link String}s in a command.
 */
public enum StringType {
	SINGLE_WORD(false),
	GREEDY_PHRASE(true);

	private final boolean all;

	StringType(boolean all) {
		this.all = all;
	}

	public boolean isAll() {
		return this.all;
	}
}
