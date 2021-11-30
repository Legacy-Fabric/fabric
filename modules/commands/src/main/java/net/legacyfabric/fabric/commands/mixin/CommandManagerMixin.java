package net.legacyfabric.fabric.commands.mixin;

import com.mojang.brigadier.tree.CommandNode;
import net.legacyfabric.fabric.commands.BrigadierCommand;
import net.legacyfabric.fabric.commands.CommandRegisterCallback;
import net.legacyfabric.fabric.commands.Commands;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin extends CommandRegistry {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void fabric_registerCommands(CallbackInfo ci) {
		MinecraftServer server = MinecraftServer.getServer();
		CommandRegisterCallback.COMMON.invoker().onCommandRegistration(Commands.DISPATCHER, server.profiler);

		if (server.isDedicated()) {
			CommandRegisterCallback.SERVER.invoker().onCommandRegistration(Commands.DISPATCHER, server, server.profiler);
		} else {
			CommandRegisterCallback.CLIENT.invoker().onCommandRegistration(Commands.DISPATCHER, MinecraftClient.getInstance(), server.profiler);
		}
	}

	/**
	 * Returns all commands registered and accessible to a source. Overwritten to include Brigadier commands.
	 *
	 * @param source the source of the command
	 * @return a list of available commands to the provided source.
	 */
	@Override
	public List<Command> method_5983(CommandSource source) {
		List<Command> commands = super.method_5983(source);

		for (CommandNode<CommandSource> commandNode : Commands.DISPATCHER.getRoot().getChildren()) {
			commands.add(new BrigadierCommand(commandNode));
		}

		return commands;
	}
}
