/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

import org.slf4j.LoggerFactory;

import net.legacyfabric.fabric.api.logger.v1.Logger;

/**
 * @author moehreag
 */
public class LoggerImpl implements Logger {
	public static final String API = "LegacyFabricAPI";
	private final org.slf4j.Logger delegate;

	public LoggerImpl(String context, String[] subs) {
		if (subs.length > 0) {
			context += "/" + String.join("/", subs);
		}

		delegate = LoggerFactory.getLogger(context);
	}

	@Override
	public void info(String format) {
		delegate.info(format);
	}

	@Override
	public void info(String format, Object... args) {
		delegate.info(String.format(format, args));
	}

	@Override
	public void info(String format, Throwable exc) {
		delegate.info(format, exc);
	}

	@Override
	public void error(String format) {
		delegate.error(format);
	}

	@Override
	public void error(String format, Object... args) {
		delegate.error(String.format(format, args));
	}

	@Override
	public void error(String format, Throwable exc) {
		delegate.error(format, exc);
	}

	@Override
	public void warn(String format) {
		delegate.warn(format);
	}

	@Override
	public void warn(String format, Object... args) {
		delegate.warn(String.format(format, args));
	}

	@Override
	public void warn(String format, Throwable exc) {
		delegate.warn(format, exc);
	}

	@Override
	public void debug(String format) {
		delegate.debug(format);
	}

	@Override
	public void debug(String format, Object... args) {
		delegate.debug(String.format(format, args));
	}

	@Override
	public void debug(String format, Throwable exc) {
		delegate.debug(format, exc);
	}

	@Override
	public void trace(String format) {
		delegate.trace(format);
	}

	@Override
	public void trace(String format, Object... args) {
		delegate.trace(String.format(format, args));
	}

	@Override
	public void trace(String format, Throwable exc) {
		delegate.trace(format, exc);
	}
}
