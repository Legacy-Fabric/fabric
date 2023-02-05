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

import org.apache.logging.log4j.LogManager;

import net.legacyfabric.fabric.api.logger.v1.Logger;

public class LoggerImpl implements Logger {
	public static final String API = "LegacyFabricAPI";
	private static final String SEPARATOR = "/";
	private final org.apache.logging.log4j.Logger log;

	public LoggerImpl(String context, String... subs) {
		String sub = String.join(SEPARATOR, subs);

		log = LogManager.getLogger("(" + context + (sub.isEmpty() ? SEPARATOR + sub : "") + ")");
	}

	public void info(String format) {
		log.info(format);
	}

	public void info(String format, Object... args) {
		log.info(format, args);
	}

	public void info(String format, Throwable exc) {
		log.info(format, exc);
	}

	public void error(String format) {
		log.error(format);
	}

	public void error(String format, Object... args) {
		log.error(format, args);
	}

	public void error(String format, Throwable exc) {
		log.error(format, exc);
	}

	public void warn(String format) {
		log.warn(format);
	}

	public void warn(String format, Object... args) {
		log.warn(format, args);
	}

	public void warn(String format, Throwable exc) {
		log.warn(format, exc);
	}

	public void debug(String format) {
		log.debug(format);
	}

	public void debug(String format, Object... args) {
		log.debug(format, args);
	}

	public void debug(String format, Throwable exc) {
		log.debug(format, exc);
	}

	public void trace(String format) {
		log.trace(format);
	}

	public void trace(String format, Object... args) {
		log.trace(format, args);
	}

	public void trace(String format, Throwable exc) {
		log.trace(format, exc);
	}
}
