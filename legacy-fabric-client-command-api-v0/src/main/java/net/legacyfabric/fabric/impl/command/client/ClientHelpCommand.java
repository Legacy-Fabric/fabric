/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.command.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.class_1981;
import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.command.NotFoundException;
import net.minecraft.command.InvalidNumberException;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import net.legacyfabric.fabric.api.command.client.FabricAbstractClientCommand;
import net.legacyfabric.fabric.api.command.client.FabricClientCommandManager;
import net.legacyfabric.fabric.api.command.client.FabricClientCommandSource;

public class ClientHelpCommand extends FabricAbstractClientCommand {
	@Override
	public void execute(FabricClientCommandSource source, String[] args) {
		final List var3 = this.method_3863(source);
		final byte var4 = 7;
		final int var5 = (var3.size() - 1) / var4;

		int i;

		try {
			i = args.length == 0 ? 0 : getClampedInt(source, args[0], 1, var5 + 1) - 1;
		} catch (InvalidNumberException var12) {
			Command cmd = (Command) this.method_3862().get(args[0]);

			if (cmd != null) {
				throw new IncorrectUsageException(cmd.getUsageTranslationKey(source));
			}

			if (MathHelper.parseInt(args[0], -1) != -1) {
				throw var12;
			}

			throw new NotFoundException();
		}

		int var7 = Math.min((i + 1) * var4, var3.size());
		TranslatableText var14 = new TranslatableText("commands.help.header", i + 1, var5 + 1);
		var14.getStyle().setFormatting(Formatting.DARK_GREEN);
		source.sendMessage(var14);

		for (int j = i * var4; j < var7; ++j) {
			Command var10 = (Command) var3.get(j);
			TranslatableText var11 = new TranslatableText(var10.getUsageTranslationKey(source), new Object[0]);
			var11.getStyle().setClickEvent(new ClickEvent(class_1981.field_8480, "/" + var10.getCommandName() + " "));
			source.sendMessage(var11);
		}

		if (i == 0 && source instanceof PlayerEntity) {
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
	public int compareTo(Object o) {
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
