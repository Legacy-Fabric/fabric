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

package io.github.legacyrewoven.mixin.networking;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import io.github.legacyrewoven.impl.networking.ChannelInfoHolder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.legacyrewoven.impl.networking.DisconnectPacketSource;
import io.github.legacyrewoven.impl.networking.PacketCallbackListener;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Mixin(ClientConnection.class)
abstract class ClientConnectionMixin implements ChannelInfoHolder {
	@Shadow
	private PacketListener packetListener;

	@Shadow
	public abstract void disconnect(Text disconnectReason);

	@Shadow
	public abstract void send(Packet<?> arg);

	@Unique
	private Collection<String> playChannels;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initAddedFields(NetworkSide side, CallbackInfo ci) {
		this.playChannels = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	@SuppressWarnings("UnnecessaryQualifiedMemberReference")
	@Redirect(method = "Lnet/minecraft/network/ClientConnection;exceptionCaught(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V"))
	private void resendOnExceptionCaught(ClientConnection clientConnection, Text disconnectReason) {
		PacketListener handler = this.packetListener;

		if (handler instanceof DisconnectPacketSource) {
			this.send(((DisconnectPacketSource) handler).createDisconnectPacket(new TranslatableText("disconnect.genericReason")));
		} else {
			this.disconnect(new TranslatableText("disconnect.genericReason")); // Don't send packet if we cannot send proper packets
		}
	}

	@Inject(method = "method_32196", at = @At(value = "INVOKE_ASSIGN", target = "Lio/netty/util/Attribute;get()Ljava/lang/Object;", remap = false))
	private void checkPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>[] genericFutureListeners, CallbackInfo ci) {
		if (this.packetListener instanceof PacketCallbackListener) {
			((PacketCallbackListener) this.packetListener).sent(packet);
		}
	}

	@Override
	public Collection<String> getPendingChannelsNames() {
		return this.playChannels;
	}
}
