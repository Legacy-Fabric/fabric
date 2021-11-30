package net.legacyfabric.fabric.test;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.legacyfabric.fabric.commands.CommandRegisterCallback;
import net.legacyfabric.fabric.commands.argument.EntityArgument;
import net.legacyfabric.fabric.commands.argument.selector.EntitySelector;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

public class CommandsTest implements ModInitializer {

	@Override
	public void onInitialize() {
		CommandRegisterCallback.COMMON.register((dispatcher, profiler) -> dispatcher.register(
				LiteralArgumentBuilder.<CommandSource>literal("foo").then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("bar", new EntityArgument())
						.executes(c -> {
							c.getSource().sendMessage(new LiteralText("Bar is " + EntityArgument.get(c, "bar")));
							return 1;
						})
				).executes(c -> {
							c.getSource().sendMessage(new LiteralText("Called foo with no arguments"));
							return 1;
						}
				)));
	}
}
