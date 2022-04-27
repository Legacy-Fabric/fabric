package net.legacyfabric.fabric.api.logger.v1;

import net.legacyfabric.fabric.impl.logger.v1.LoggerImpl;

public interface Logger {
	static Logger get(String context, String... args) {
		return new LoggerImpl(context, args);
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
