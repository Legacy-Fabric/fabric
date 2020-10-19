package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;
import net.fabricmc.fabric.api.command.v2.event.CommandRegistrationCallback;
import net.fabricmc.fabric.impl.command.InternalObjects;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}

	@Inject(at = @At("HEAD"), method = "createCommandManager")
	public void initCommands(CallbackInfoReturnable<CommandManager> cir) {
		CommandRegistrationCallback.EVENT.invoker().accept(InternalObjects.getCommandManager(), (MinecraftServer) (Object) this);
	}
}
