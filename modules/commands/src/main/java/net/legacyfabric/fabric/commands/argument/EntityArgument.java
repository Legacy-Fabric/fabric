package net.legacyfabric.fabric.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.legacyfabric.fabric.commands.argument.selector.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PlayerSelector;

/**
 * Represents an {@link net.legacyfabric.fabric.commands.argument.selector.EntitySelector} as an argument within a command.
 */
public class EntityArgument implements ArgumentType<EntitySelector> {

	@Override
	public EntitySelector parse(StringReader reader) {
		EntitySelector selector = new EntitySelector(PlayerSelector.method_6705(MinecraftServer.getServer(), reader.getString(), Entity.class));
		// Skip until the next argument. FIXME: this is a hack. Probably a better solution somewhere.
		while (reader.peek() != ' ' || reader.getRemainingLength() != 0) {
			reader.skip();
		}
		return selector;
	}

	public static EntitySelector get(final CommandContext<?> context, final String name) {
		return context.getArgument(name, EntitySelector.class);
	}
}
