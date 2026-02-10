/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.impl.logger;

import java.util.function.BiFunction;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.utils.TriState;

public class LoggerHelper {
	public static final String API = "LegacyFabricAPI";

	private static TriState exists = TriState.DEFAULT;
	private static final BiFunction<String, String[], Logger> javaLogger = JavaLoggerImpl::new;
	private static final BiFunction<String, String[], Logger> nativeLogger = NativeLoggerImpl::new;

	public static Logger getLogger(String context, String... subs) {
		if (exists == TriState.DEFAULT) {
			try {
				Class.forName("net.fabricmc.loader.impl.util.log.LogCategory");
				Class.forName("net.fabricmc.loader.impl.util.log.Log");
				exists = TriState.TRUE;
			} catch (ClassNotFoundException ignored) {
				exists = TriState.FALSE;
			}
		}

		return exists.get() ? nativeLogger.apply(context, subs) : javaLogger.apply(context, subs);
	}
}
