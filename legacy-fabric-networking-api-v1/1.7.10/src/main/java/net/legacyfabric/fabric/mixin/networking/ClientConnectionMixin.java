/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.mixin.networking;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.Connection;
import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import net.legacyfabric.fabric.impl.networking.ChannelInfoHolder;
import net.legacyfabric.fabric.impl.networking.ClientConnectionExtension;
import net.legacyfabric.fabric.impl.networking.DisconnectPacketSource;
import net.legacyfabric.fabric.impl.networking.PacketCallbackListener;

@Mixin(Connection.class)
public abstract class ClientConnectionMixin implements ChannelInfoHolder, ClientConnectionExtension {
	@Shadow
	private PacketHandler listener;

	@Shadow
	public abstract void disconnect(Text disconnectReason);

	@Shadow
	public abstract void send(Packet par1, GenericFutureListener<?>... par2);

	@Shadow
	@Final
	private static Logger LOGGER;
	@Unique
	private Collection<String> playChannels;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initAddedFields(boolean par1, CallbackInfo ci) {
		this.playChannels = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	@SuppressWarnings("UnnecessaryQualifiedMemberReference")
	@Redirect(method = "Lnet/minecraft/network/Connection;exceptionCaught(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;disconnect(Lnet/minecraft/text/Text;)V"))
	private void resendOnExceptionCaught(Connection clientConnection, Text disconnectReason, @Local(argsOnly = true) Throwable throwable) {
		PacketHandler handler = this.listener;

		LOGGER.debug("Internal Exception: {}", disconnectReason.getContent(), throwable);

		if (handler instanceof DisconnectPacketSource) {
			this.send(((DisconnectPacketSource) handler).createDisconnectPacket(new TranslatableText("disconnect.genericReason", "Internal Exception: " + disconnectReason.getContent())));
		} else {
			this.disconnect(new TranslatableText("disconnect.genericReason", "Internal Exception: " + disconnectReason.getContent())); // Don't send packet if we cannot send proper packets
		}
	}

	@Inject(method = "doSend", at = @At(value = "INVOKE_ASSIGN", target = "Lio/netty/util/Attribute;get()Ljava/lang/Object;", remap = false))
	private void checkPacket(Packet packet, GenericFutureListener<?>[] genericFutureListeners, CallbackInfo ci) {
		if (this.listener instanceof PacketCallbackListener) {
			((PacketCallbackListener) this.listener).sent(packet);
		}
	}

	@Override
	public Collection<String> getPendingChannelsNames() {
		return this.playChannels;
	}

	@Override
	public void sendPacket(Packet packet) {
		this.send(packet);
	}
}
