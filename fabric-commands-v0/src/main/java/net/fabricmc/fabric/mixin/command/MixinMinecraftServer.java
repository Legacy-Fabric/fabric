package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import net.fabricmc.fabric.api.event.server.FabricCommandRegisteredCallback;
import net.fabricmc.fabric.impl.command.FabricCommandRegistryImpl;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
	@Shadow
	public abstract boolean isDedicated();

	@Inject(at = @At("TAIL"), method = "createCommandManager")
	public void interceptCommands(CallbackInfoReturnable<CommandManager> cir) {
		FabricCommandRegistryImpl.getCommandMap().forEach((command, side) -> {
			boolean dedicated = this.isDedicated();

			if (!(dedicated) && side.isIntegrated()) {
				cir.getReturnValue().registerCommand(command);
			}

			if (dedicated && side.isDedicated()) {
				cir.getReturnValue().registerCommand(command);
			}

			FabricCommandRegisteredCallback.EVENT.invoker().onCommandRegistered(MinecraftServer.getServer(), command, side);
		});
	}
}
