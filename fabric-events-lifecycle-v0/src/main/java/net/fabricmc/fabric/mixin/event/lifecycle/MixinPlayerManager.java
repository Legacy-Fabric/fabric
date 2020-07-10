/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.mixin.event.lifecycle;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.server.PlayerConnectCallback;
import net.fabricmc.fabric.api.event.server.PlayerJoinCallback;
import net.fabricmc.fabric.util.LoginMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
	@Shadow
	public abstract void sendToAll(Text text);

	@Shadow
	public abstract void method_6220(ServerPlayerEntity player);

	@Shadow
	public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at=@At("HEAD"),method = "onPlayerConnect")
	public void playerConnectCallback(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info){
		PlayerConnectCallback.EVENT.invoker().playerConnect(connection,player);
	}

	@Inject(at=@At(value = "INVOKE", target = "Lnet/minecraft/text/TranslatableText;<init>(Ljava/lang/String;[Ljava/lang/Object;)V"), method = "onPlayerConnect", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void playerJoinCallback(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci, GameProfile gameProfile, UserCache userCache, String string, CompoundTag compoundTag, String string2, ServerWorld serverWorld, LevelProperties levelProperties, BlockPos blockPos, ServerPlayNetworkHandler serverPlayNetworkHandler) {
		ci.cancel();
		Text originalText = new TranslatableText("multiplayer.player.joined", player.getName(), string);
		originalText.getStyle().setColor(Formatting.YELLOW);
		Text joinText = PlayerJoinCallback.EVENT.invoker().playerJoin(originalText, player);
		this.sendToAll(joinText);

		this.method_6220(player);
		serverPlayNetworkHandler.requestTeleport(player.x, player.y, player.z, player.yaw, player.pitch);
		this.sendWorldInfo(player, serverWorld);
		if (this.server.getResourcePackUrl().length() > 0) {
			player.method_6068(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
		}

		Iterator iterator = player.method_7134().iterator();

		while(iterator.hasNext()) {
			StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator.next();
			serverPlayNetworkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getEntityId(), statusEffectInstance));
		}

		player.method_6074();
		if (compoundTag != null && compoundTag.contains("Riding", 10)) {
			Entity entity = EntityType.method_7068(compoundTag.getCompound("Riding"), serverWorld);
			if (entity != null) {
				entity.field_7393 = true;
				serverWorld.spawnEntity(entity);
				player.startRiding(entity);
				entity.field_7393 = false;
			}
		}
	}
}
