package net.legacyfabric.fabric.commands.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.legacyfabric.fabric.commands.Commands;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin {

	private static final Style ERROR_STYLE = new Style().setColor(Formatting.RED);

	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private void fabric_brigadierExecuteHook(CommandSource source, String command, CallbackInfoReturnable<Integer> cir) {
		StringReader stringReader = new StringReader(command);
		if (stringReader.canRead() && stringReader.peek() == '/') {
			stringReader.skip();
		}

		try {
			cir.setReturnValue(Commands.DISPATCHER.execute(stringReader, source));
		} catch (CommandSyntaxException e) {
			boolean isBrigadierCommand = !e.getMessage().startsWith("Unknown command at");
			if (source.sendCommandFeedback() && isBrigadierCommand) {
				source.sendMessage(new LiteralText(e.getMessage()).setStyle(ERROR_STYLE));
				e.printStackTrace();
				System.out.println(stringReader.getString());
				cir.setReturnValue(0);
			}
		}
	}
}
