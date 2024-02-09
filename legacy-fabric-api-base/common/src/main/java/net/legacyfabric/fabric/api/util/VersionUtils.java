/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.api.util;

import java.util.NoSuchElementException;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public class VersionUtils {
	public static boolean matches(String modId, String versionPredicate) {
		boolean result = false;

		try {
			VersionPredicate predicate = VersionPredicate.parse(versionPredicate);

			result = predicate.test(FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion());
		} catch (VersionParsingException | NoSuchElementException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean matches(String versionPredicate) {
		return matches("minecraft", versionPredicate);
	}
}
