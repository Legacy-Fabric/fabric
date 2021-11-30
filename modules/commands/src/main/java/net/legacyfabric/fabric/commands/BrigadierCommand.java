package net.legacyfabric.fabric.commands;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BrigadierCommand implements Command {

	private final CommandNode<CommandSource> commandNode;
	private final String name;

	public BrigadierCommand(CommandNode<CommandSource> commandNode) {
		this.commandNode = commandNode;
		this.name = commandNode.getName();
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return name;
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSource source, String[] args) {
	}

	@Override
	public boolean isAccessible(CommandSource source) {
		return commandNode.canUse(source);
	}

	@Override
	public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
		//TODO: autocomplete suggestions
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameAtIndex(String[] args, int index) {
		return false;
	}

	@Override
	public int compareTo(@NotNull Command o) {
		return 0;
	}
}
