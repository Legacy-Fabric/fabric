package net.legacyfabric.fabric.impl.logger;

import net.legacyfabric.fabric.api.logger.v1.Logger;

public class LoggerHelper {
	public static Logger getLogger(String context, String... subs) {
		try {
			return new NativeLoggerImpl(context, subs);
		} catch (NoClassDefFoundError e) {
			return new JavaLoggerImpl(context, subs);
		}
	}
}
