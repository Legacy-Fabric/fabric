package net.fabricmc.fabric.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.commands.ServerCommandSource;
import net.fabricmc.fabric.impl.commands.CommandManagerHolder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin {

	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private void execute(CommandSource source, String command, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		CommandDispatcher<ServerCommandSource> dispatcher = CommandManagerHolder.COMMAND_DISPATCHER;
		boolean removeSlash = command.startsWith("/");

		{
			String[] split = (removeSlash ? command.substring(1) : command).split(" ");

			if (split.length < 1 || dispatcher.findNode(Collections.singleton(split[0])) == null) {
				return;
			}
		}

		try {
			StringReader reader = new StringReader(command);

			if (removeSlash) {
				reader.skip();
			}

			callbackInfoReturnable.setReturnValue(dispatcher.execute(reader, ServerCommandSource.from(source)));
		} catch (CommandSyntaxException exception) {
			source.sendMessage(new LiteralText(exception.getMessage()));
			callbackInfoReturnable.setReturnValue(-1);
		}
	}
}
