package net.legacyfabric.fabric.api.command.v2.lib.sponge.args;

import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

/**
 * Parent class that specifies elements as having no tab completions.
 * Useful for inputs with a very large domain, like strings and integers.
 */
public abstract class KeyElement extends CommandElement {
	protected KeyElement(Text key) {
		super(key);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		return Collections.emptyList();
	}
}
