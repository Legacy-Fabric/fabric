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

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

import net.legacyfabric.fabric.api.logger.v1.Logger;

public class NativeLoggerImpl implements Logger {
	private final LogCategory category;

	public NativeLoggerImpl(String context, String... subs) {
		this.category = LogCategory.createCustom(context, subs);
	}

	public void info(String format) {
		Log.info(this.category, format);
	}

	public void info(String format, Object... args) {
		Log.info(this.category, format, args);
	}

	public void info(String format, Throwable exc) {
		Log.info(this.category, format, exc);
	}

	public void error(String format) {
		Log.error(this.category, format);
	}

	public void error(String format, Object... args) {
		Log.error(this.category, format, args);
	}

	public void error(String format, Throwable exc) {
		Log.error(this.category, format, exc);
	}

	public void warn(String format) {
		Log.warn(this.category, format);
	}

	public void warn(String format, Object... args) {
		Log.warn(this.category, format, args);
	}

	public void warn(String format, Throwable exc) {
		Log.warn(this.category, format, exc);
	}

	public void debug(String format) {
		Log.debug(this.category, format);
	}

	public void debug(String format, Object... args) {
		Log.debug(this.category, format, args);
	}

	public void debug(String format, Throwable exc) {
		Log.debug(this.category, format, exc);
	}

	public void trace(String format) {
		Log.trace(this.category, format);
	}

	public void trace(String format, Object... args) {
		Log.trace(this.category, format, args);
	}

	public void trace(String format, Throwable exc) {
		Log.trace(this.category, format, exc);
	}
}
