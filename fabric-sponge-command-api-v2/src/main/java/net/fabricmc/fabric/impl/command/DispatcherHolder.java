package net.fabricmc.fabric.impl.command;

import net.fabricmc.fabric.api.command.v2.lib.sponge.dispatcher.Dispatcher;
import net.fabricmc.fabric.api.command.v2.lib.sponge.dispatcher.SimpleDispatcher;

public class DispatcherHolder {
	private static final Dispatcher DISPATCHER = new SimpleDispatcher();
}
