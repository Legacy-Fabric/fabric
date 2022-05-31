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

package net.legacyfabric.fabric.api.logger.v1;

import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public interface Logger {
	/**
	 * Create a Logger implementation with provided context and subs.
	 *
	 * <p>Example with info: [01:06:31] [main/INFO] (FabricAPI/Test/ClientLifecycleEvents) Client started
	 * <br>context = "FabricAPI"
	 * <br>subs = ["Test", "ClientLifecycleEvents"]
	 *
	 * @param context Main logger name
	 * @param subs Sub logger name, optional
	 * @return a Logger implementation
	 */
	static Logger get(String context, String... subs) {
		return new LoggerImpl(context, subs);
	}

	void info(String format);

	void info(String format, Object... args);

	void info(String format, Throwable exc);

	void error(String format);

	void error(String format, Object... args);

	void error(String format, Throwable exc);

	void warn(String format);

	void warn(String format, Object... args);

	void warn(String format, Throwable exc);

	void debug(String format);

	void debug(String format, Object... args);

	void debug(String format, Throwable exc);

	void trace(String format);

	void trace(String format, Object... args);

	void trace(String format, Throwable exc);
}
