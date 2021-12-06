package io.github.legacyrewoven.api.command.v2.lib.sponge.args;

import java.util.Collections;
import java.util.List;

import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;

import net.minecraft.text.Text;

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
