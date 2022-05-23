package net.legacyfabric.fabric.api.client.commands;

import net.legacyfabric.fabric.impl.client.commands.ClientHelpCommand;
import net.legacyfabric.fabric.impl.client.commands.ClientSetPrefixCommand;
import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.PlayerSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricClientCommandManager extends CommandRegistry implements CommandProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final FabricClientCommandManager INSTANCE = new FabricClientCommandManager();
	public String PREFIX = "/$";

	private FabricClientCommandManager() {
		this.registerCommand(new ClientHelpCommand());
		this.registerCommand(new ClientSetPrefixCommand());
	}

	@Override
	public void run(CommandSource sender, Command command, int permissionLevel, String label, Object... args) {
		TranslatableText var7 = new TranslatableText("chat.type.admin", sender.getTranslationKey(), new TranslatableText(label, args));
		var7.getStyle().setFormatting(Formatting.GRAY);
		var7.getStyle().setItalic(true);
		if (sender instanceof FabricClientCommandSource) {
			sender.sendMessage(var7);
		} else {
			LOGGER.error("Warning! Client command manager detect invalid run function call! Text: {}", var7.asString());
		}
	}

	public int execute(FabricClientCommandSource source, String name) {
		name = name.trim();
		if (name.startsWith(INSTANCE.PREFIX)) {
			name = name.substring(INSTANCE.PREFIX.length());
		}

		String[] var3 = name.split(" ");
		String var4 = var3[0];
		var3 = method_3104(var3);
		Command var5 = (Command) this.getCommandMap().get(var4);
		int var6 = this.method_4642(var5, var3);
		int var7 = 0;

		TranslatableText var9;
		try {
			if (var5 == null) {
				throw new NotFoundException();
			}

			if (var5.isAccessible(source)) {
				if (var6 > -1) {
					ServerPlayerEntity[] var8 = PlayerSelector.selectPlayers(source, var3[var6]);
					String var22 = var3[var6];
					ServerPlayerEntity[] var10 = var8;
					int var11 = var8.length;

					for(int var12 = 0; var12 < var11; ++var12) {
						ServerPlayerEntity var13 = var10[var12];
						var3[var6] = var13.getTranslationKey();

						try {
							var5.execute(source, var3);
							++var7;
						} catch (CommandException var17) {
							TranslatableText var15 = new TranslatableText(var17.getMessage(), var17.getArgs());
							var15.getStyle().setFormatting(Formatting.RED);
							source.sendMessage(var15);
						}
					}

					var3[var6] = var22;
				} else {
					try {
						var5.execute(source, var3);
						++var7;
					} catch (CommandException var16) {
						var9 = new TranslatableText(var16.getMessage(), var16.getArgs());
						var9.getStyle().setFormatting(Formatting.RED);
						source.sendMessage(var9);
					}
				}
			} else {
				TranslatableText var21 = new TranslatableText("commands.generic.permission", new Object[0]);
				var21.getStyle().setFormatting(Formatting.RED);
				source.sendMessage(var21);
			}
		} catch (IncorrectUsageException var18) {
			var9 = new TranslatableText("commands.generic.usage", new Object[]{new TranslatableText(var18.getMessage(), var18.getArgs())});
			var9.getStyle().setFormatting(Formatting.RED);
			source.sendMessage(var9);
		} catch (CommandException var19) {
			var9 = new TranslatableText(var19.getMessage(), var19.getArgs());
			var9.getStyle().setFormatting(Formatting.RED);
			source.sendMessage(var9);
		} catch (Throwable var20) {
			var9 = new TranslatableText("commands.generic.exception", new Object[0]);
			var9.getStyle().setFormatting(Formatting.RED);
			source.sendMessage(var9);
			LOGGER.error("Couldn't process command: '" + name + "'", var20);
		}

		return var7;
	}

	private static String[] method_3104(String[] strings) {
		String[] var1 = new String[strings.length - 1];

		for(int var2 = 1; var2 < strings.length; ++var2) {
			var1[var2 - 1] = strings[var2];
		}

		return var1;
	}

	private int method_4642(Command command, String[] args) {
		if (command == null) {
			return -1;
		} else {
			for(int var3 = 0; var3 < args.length; ++var3) {
				if (command.isUsernameAtIndex(args, var3) && PlayerSelector.method_4088(args[var3])) {
					return var3;
				}
			}

			return -1;
		}
	}
}
