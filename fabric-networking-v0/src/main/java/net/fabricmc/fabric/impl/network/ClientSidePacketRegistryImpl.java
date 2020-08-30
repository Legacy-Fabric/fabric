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

package net.fabricmc.fabric.impl.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.network.S2CPacketTypeCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.PacketIdentifier;

@Environment(EnvType.CLIENT)
public class ClientSidePacketRegistryImpl extends PacketRegistryImpl implements ClientSidePacketRegistry {
	private final Collection<String> serverPayloadIds = Sets.newHashSet();

	public static void invalidateRegisteredIdList() {
		((ClientSidePacketRegistryImpl) ClientSidePacketRegistry.INSTANCE).serverPayloadIds.clear();
	}

	@Override
	public boolean canServerReceive(String id) {
		return serverPayloadIds.contains(id);
	}

	@Override
	public boolean canServerReceive(PacketIdentifier id) {
		return this.canServerReceive(id.toString());
	}

	@Override
	public void sendToServer(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

		if (handler != null) {
			if (completionListener == null) {
				handler.sendPacket(packet);
			} else {
				handler.getClientConnection().send(packet, completionListener);
			}
		} else {
			LOGGER.warn("Sending packet " + packet + " to server failed, not connected!");
		}
	}

	@Override
	protected void onRegister(String id) {
		ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

		if (handler != null) {
			createRegisterTypePacket(PacketTypes.REGISTER, Collections.singleton(id)).ifPresent(handler::sendPacket);
		}
	}

	@Override
	protected void onUnregister(String id) {
		ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

		if (handler != null) {
			createRegisterTypePacket(PacketTypes.UNREGISTER, Collections.singleton(id)).ifPresent(handler::sendPacket);
		}
	}

	@Override
	protected Collection<String> getIdCollectionFor(PacketContext context) {
		return serverPayloadIds;
	}

	@Override
	protected void onReceivedRegisterPacket(PacketContext context, Collection<String> ids) {
		S2CPacketTypeCallback.REGISTERED.invoker().accept(ids);
	}

	@Override
	protected void onReceivedUnregisterPacket(PacketContext context, Collection<String> ids) {
		S2CPacketTypeCallback.UNREGISTERED.invoker().accept(ids);
	}

	@Override
	public Packet<?> toPacket(String id, PacketByteBuf buf) {
		return new CustomPayloadC2SPacket(id, buf);
	}

	@Override
	public Packet<?> toPacket(PacketIdentifier id, PacketByteBuf buf) {
		return this.toPacket(id.toString(), buf);
	}
}
