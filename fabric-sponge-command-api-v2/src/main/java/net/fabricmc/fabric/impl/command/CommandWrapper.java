package net.fabricmc.fabric.impl.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.command.v2.Location;
import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandException;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandMapping;

class CommandWrapper extends AbstractCommand {
	private final CommandMapping mapping;

	public CommandWrapper(CommandMapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public String getCommandName() {
		return this.mapping.getPrimaryAlias();
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(this.mapping.getAllAliases());
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return this.mapping.getCallable().getHelp((PermissibleCommandSource) source).map(Text::asString).orElse("");
	}

	@Override
	public void execute(CommandSource source, String[] args) {
		try {
			this.mapping.getCallable().process((PermissibleCommandSource) source, String.join(" ", args));
		} catch (CommandException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isAccessible(CommandSource source) {
		return this.mapping.getCallable().testPermission((PermissibleCommandSource) source);
	}

	@Override
	public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
		try {
			return this.mapping.getCallable().getSuggestions((PermissibleCommandSource) source, Arrays.stream(args).collect(Collectors.joining(" ")), new Location<>(source.getWorld(), pos));
		} catch (CommandException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
