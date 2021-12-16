/*
 * Copyright (c) 2021 Legacy Rewoven
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
import io.github.legacyrewoven.impl.networking.DisconnectPacketSource;
import io.github.legacyrewoven.impl.networking.PacketCallbackListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
//import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Mixin(ClientConnection.class)
abstract class ClientConnectionMixin implements ChannelInfoHolder {
	@Shadow
	private PacketListener field_5964;

	@Shadow
	public abstract void disconnect(Text disconnectReason);

	@Shadow
	public abstract void method_9851(Packet packet, GenericFutureListener... genericFutureListeners);

	@Unique
	private Collection<String> playChannels;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initAddedFields(boolean networkSide, CallbackInfo ci) {
		this.playChannels = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	@SuppressWarnings("UnnecessaryQualifiedMemberReference")
	@Redirect(method = "Lnet/minecraft/network/ClientConnection;exceptionCaught(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V"))
	private void resendOnExceptionCaught(ClientConnection clientConnection, Text disconnectReason) {
		PacketListener handler = this.field_5964;

		if (handler instanceof DisconnectPacketSource) {
			this.method_9851(((DisconnectPacketSource) handler).createDisconnectPacket(new TranslatableText("disconnect.genericReason")));
		} else {
			this.disconnect(new TranslatableText("disconnect.genericReason")); // Don't send packet if we cannot send proper packets
		}
	}

	@Inject(method = "method_5100", at = @At(value = "INVOKE_ASSIGN", target = "Lio/netty/util/Attribute;get()Ljava/lang/Object;", remap = false))
	private void checkPacket(Packet packet, GenericFutureListener<? extends Future<? super Void>>[] genericFutureListeners, CallbackInfo ci) {
		if (this.field_5964 instanceof PacketCallbackListener) {
			((PacketCallbackListener) this.field_5964).sent(packet);
		}
	}

	@Override
	public Collection<String> getPendingChannelsNames() {
		return this.playChannels;
	}
}
