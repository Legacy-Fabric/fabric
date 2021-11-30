package net.legacyfabric.fabric.test;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.legacyfabric.fabric.commands.CommandRegisterCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class CommandsTest implements ModInitializer {

	@Override
	public void onInitialize() {
		CommandRegisterCallback.COMMON.register((dispatcher, profiler) -> dispatcher.register(
				LiteralArgumentBuilder.<CommandSource>literal("foo")
						.then(
								RequiredArgumentBuilder.<CommandSource, Integer>argument("bar", integer())
										.executes(c -> {
											c.getSource().sendMessage(new LiteralText("Bar is " + getInteger(c, "bar")));
											return 1;
										})
						)
						.executes(c -> {
							c.getSource().sendMessage(new LiteralText("Called foo with no arguments"));
							return 1;
						})
		));
	}
}
