/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JavaLoggerImpl implements net.legacyfabric.fabric.api.logger.v1.Logger {
	private final Logger instance;

	protected JavaLoggerImpl(String context, String... subs) {
		List<String> parts = new ArrayList<>();
		parts.add(context);
		parts.addAll(Arrays.asList(subs));

		instance = Logger.getLogger(parts.stream().collect(Collectors.joining("/")));
	}

	@Override
	public void info(String format) {
		instance.log(Level.INFO, format);
	}

	@Override
	public void info(String format, Object... args) {
		instance.log(Level.INFO, format, args);
	}

	@Override
	public void info(String format, Throwable exc) {
		instance.log(Level.INFO, format, exc);
	}

	@Override
	public void error(String format) {
		instance.log(Level.SEVERE, format);
	}

	@Override
	public void error(String format, Object... args) {
		instance.log(Level.SEVERE, format, args);
	}

	@Override
	public void error(String format, Throwable exc) {
		instance.log(Level.SEVERE, format, exc);
	}

	@Override
	public void warn(String format) {
		instance.log(Level.WARNING, format);
	}

	@Override
	public void warn(String format, Object... args) {
		instance.log(Level.WARNING, format, args);
	}

	@Override
	public void warn(String format, Throwable exc) {
		instance.log(Level.WARNING, format, exc);
	}

	@Override
	public void debug(String format) {
		instance.log(Level.FINE, format);
	}

	@Override
	public void debug(String format, Object... args) {
		instance.log(Level.FINE, format, args);
	}

	@Override
	public void debug(String format, Throwable exc) {
		instance.log(Level.FINE, format, exc);
	}

	@Override
	public void trace(String format) {
		instance.log(Level.FINEST, format);
	}

	@Override
	public void trace(String format, Object... args) {
		instance.log(Level.FINEST, format, args);
	}

	@Override
	public void trace(String format, Throwable exc) {
		instance.log(Level.FINEST, format, exc);
	}
}
