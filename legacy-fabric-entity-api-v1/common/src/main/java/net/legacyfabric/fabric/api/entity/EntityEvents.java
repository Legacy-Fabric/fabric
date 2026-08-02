package net.legacyfabric.fabric.api.entity;

import net.ornithemc.osl.core.api.events.Event;

public class EntityEvents {
	public static final Event<Runnable> REGISTER_ENTITIES = Event.runnable();
}
