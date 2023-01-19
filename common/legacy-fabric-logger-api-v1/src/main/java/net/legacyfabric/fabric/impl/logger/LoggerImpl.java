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

package net.legacyfabric.fabric.impl.logger;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggerImpl implements Logger {
	public static final String API = "LegacyFabricAPI";
	private static final String SEPARATOR = "/";
	private final org.apache.logging.log4j.Logger Log;

	public LoggerImpl(String context, String... subs) {
		String sub = String.join(SEPARATOR, subs);

		Log = LogManager.getLogger("("+context + (sub.isEmpty() ? SEPARATOR+sub : "")+")");
	}

	public void info(String format) {
		Log.info(format);
	}

	public void info(String format, Object... args) {
		Log.info(format, args);
	}

	public void info(String format, Throwable exc) {
		Log.info(format, exc);
	}

	public void error(String format) {
		Log.error(format);
	}

	public void error(String format, Object... args) {
		Log.error(format, args);
	}

	public void error(String format, Throwable exc) {
		Log.error(format, exc);
	}

	public void warn(String format) {
		Log.warn(format);
	}

	public void warn(String format, Object... args) {
		Log.warn(format, args);
	}

	public void warn(String format, Throwable exc) {
		Log.warn(format, exc);
	}

	public void debug(String format) {
		Log.debug(format);
	}

	public void debug(String format, Object... args) {
		Log.debug(format, args);
	}

	public void debug(String format, Throwable exc) {
		Log.debug(format, exc);
	}

	public void trace(String format) {
		Log.trace(format);
	}

	public void trace(String format, Object... args) {
		Log.trace(format, args);
	}

	public void trace(String format, Throwable exc) {
		Log.trace(format, exc);
	}
}
