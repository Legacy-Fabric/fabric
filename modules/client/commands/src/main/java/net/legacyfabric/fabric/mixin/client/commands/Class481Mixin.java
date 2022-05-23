package net.legacyfabric.fabric.mixin.client.commands;

import net.legacyfabric.fabric.api.client.commands.FabricClientCommandManager;
import net.legacyfabric.fabric.api.client.commands.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.Command;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.class_481;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_481.class)
public class Class481Mixin extends ClientPlayerEntity {
	public Class481Mixin(MinecraftClient minecraftClient, World world, Session session, int i) {
		super(minecraftClient, world, session, i);
	}

	@Inject(method = "method_1262", at = @At("HEAD"), cancellable = true)
	private void method_1262(String str, CallbackInfo ci) {
		if (str.startsWith(FabricClientCommandManager.INSTANCE.PREFIX)) {
			String cmd = str.substring(FabricClientCommandManager.INSTANCE.PREFIX.length()).split(" ")[0];
			if (FabricClientCommandManager.INSTANCE.getCommandMap().containsKey(cmd)) {
				Command command = ((Command) FabricClientCommandManager.INSTANCE.getCommandMap().get(cmd));
				FabricClientCommandManager.INSTANCE.execute(new FabricClientCommandSource() {
					@Override
					public void sendFeedback(Text message) {
						Class481Mixin.this.sendMessage(message);
					}

					@Override
					public void sendError(Text message) {
						Class481Mixin.this.sendMessage(message);
					}

					@Override
					public MinecraftClient getClient() {
						return Class481Mixin.this.client;
					}

					@Override
					public ClientWorld getWorld() {
						return (ClientWorld) Class481Mixin.this.world;
					}

					@Override
					public String getTranslationKey() {
						return command.getUsageTranslationKey(this);
					}

					@Override
					public Text getName() {
						return new LiteralText(command.getCommandName());
					}

					@Override
					public void sendMessage(Text text) {
						Class481Mixin.this.sendMessage(text);
					}

					@Override
					public boolean canUseCommand(int permissionLevel, String commandLiteral) {
						return false;
					}

					@Override
					public BlockPos method_4086() {
						return Class481Mixin.this.method_4086();
					}
				}, str);
				ci.cancel();
			}
		}
	}
}
