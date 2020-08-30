package net.fabricmc.fabric.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.DispatcherRegistrationCallback;
import net.fabricmc.fabric.impl.commands.CommandManagerHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void postInitialize(CallbackInfo callbackInfo) {
		DispatcherRegistrationCallback.EVENT.invoker().initialize(
				CommandManagerHolder.COMMAND_DISPATCHER = new CommandDispatcher<>(),
				MinecraftServer.getServer().isDedicated());
	}
}
