package net.fabricmc.fabric.api.command.v2;

import net.minecraft.command.CommandSource;

/**
 * Represents a {@link CommandSource} that may or may not be able to run
 * a command as they do not have the permission to do so.
 */
public interface PermissibleCommandSource extends CommandSource {
	boolean hasPermission(String perm);
}
