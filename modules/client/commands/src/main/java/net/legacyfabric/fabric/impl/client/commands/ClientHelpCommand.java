package net.legacyfabric.fabric.impl.client.commands;

import net.legacyfabric.fabric.api.client.commands.FabricAbstractClientCommand;
import net.legacyfabric.fabric.api.client.commands.FabricClientCommandManager;
import net.legacyfabric.fabric.api.client.commands.FabricClientCommandSource;
import net.minecraft.class_1981;
import net.minecraft.command.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClientHelpCommand extends FabricAbstractClientCommand {
	@Override
	public void execute(FabricClientCommandSource source, String[] args) {
		final List var3 = this.method_3863(source);
		final byte var4 = 7;
		final int var5 = (var3.size() - 1) / var4;

		int var13;
		try {
			var13 = args.length == 0 ? 0 : getClampedInt(source, args[0], 1, var5 + 1) - 1;
		} catch (InvalidNumberException var12) {
			Command var9 = (Command) this.method_3862().get(args[0]);
			if (var9 != null) {
				throw new IncorrectUsageException(var9.getUsageTranslationKey(source));
			}

			if (MathHelper.parseInt(args[0], -1) != -1) {
				throw var12;
			}

			throw new NotFoundException();
		}

		int var7 = Math.min((var13 + 1) * var4, var3.size());
		TranslatableText var14 = new TranslatableText("commands.help.header", var13 + 1, var5 + 1);
		var14.getStyle().setFormatting(Formatting.DARK_GREEN);
		source.sendMessage(var14);

		for(int var15 = var13 * var4; var15 < var7; ++var15) {
			Command var10 = (Command)var3.get(var15);
			TranslatableText var11 = new TranslatableText(var10.getUsageTranslationKey(source), new Object[0]);
			var11.getStyle().setClickEvent(new ClickEvent(class_1981.field_8480, "/" + var10.getCommandName() + " "));
			source.sendMessage(var11);
		}

		if (var13 == 0 && source instanceof PlayerEntity) {
			TranslatableText var16 = new TranslatableText("commands.help.footer");
			var16.getStyle().setFormatting(Formatting.GREEN);
			source.sendMessage(var16);
		}
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return "commands.help.usage";
	}

	@Override
	public int compareTo(@NotNull Object o) {
		return 0;
	}

	protected List method_3863(CommandSource commandSource) {
		List var2 = FabricClientCommandManager.INSTANCE.method_3309(commandSource);
		Collections.sort(var2);
		return var2;
	}

	protected Map method_3862() {
		return FabricClientCommandManager.INSTANCE.getCommandMap();
	}
}
