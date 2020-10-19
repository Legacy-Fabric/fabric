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

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

import net.fabricmc.fabric.api.event.network.C2SPacketTypeCallback;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.PacketIdentifier;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerSidePacketRegistryImpl extends PacketRegistryImpl implements ServerSidePacketRegistry {
	private final WeakHashMap<PlayerEntity, Collection<String>> playerPayloadIds = new WeakHashMap<>();
	private final Set<WeakReference<ServerPlayNetworkHandler>> handlers = Sets.newHashSet();

	public void onQueryResponse(LoginHelloC2SPacket packet) {
	}

	public void addNetworkHandler(ServerPlayNetworkHandler handler) {
		this.handlers.add(new WeakReference<>(handler));
	}

	protected void forEachHandler(Consumer<ServerPlayNetworkHandler> consumer) {
		Iterator<WeakReference<ServerPlayNetworkHandler>> it = this.handlers.iterator();

		while (it.hasNext()) {
			ServerPlayNetworkHandler server = it.next().get();

			if (server != null) {
				consumer.accept(server);
			} else {
				it.remove();
			}
		}
	}

	@Override
	public boolean canPlayerReceive(PlayerEntity player, String id) {
		Collection<String> ids = this.playerPayloadIds.get(player);

		if (ids != null) {
			return ids.contains(id);
		} else {
			return false;
		}
	}

	@Override
	public boolean canPlayerReceive(PlayerEntity player, PacketIdentifier id) {
		return this.canPlayerReceive(player, id.toString());
	}

	@Override
	public void sendToPlayer(PlayerEntity player, Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		if (!(player instanceof ServerPlayerEntity)) {
			throw new RuntimeException("Can only send to ServerPlayerEntities!");
		} else {
			if (completionListener != null) {
				((ServerPlayerEntity) player).networkHandler.connection.send(packet, completionListener);
				return;
			}

			((ServerPlayerEntity) player).networkHandler.sendPacket(packet);
		}
	}

	@Override
	protected void onRegister(String id) {
		this.createRegisterTypePacket(PacketTypes.REGISTER, Collections.singleton(id)).ifPresent((packet) -> this.forEachHandler((n) -> n.sendPacket(packet)));
	}

	@Override
	protected void onUnregister(String id) {
		this.createRegisterTypePacket(PacketTypes.UNREGISTER, Collections.singleton(id)).ifPresent((packet) -> this.forEachHandler((n) -> n.sendPacket(packet)));
	}

	@Override
	protected Collection<String> getIdCollectionFor(PacketContext context) {
		return this.playerPayloadIds.computeIfAbsent(context.getPlayer(), (p) -> new HashSet<>());
	}

	@Override
	protected void onReceivedRegisterPacket(PacketContext context, Collection<String> ids) {
		C2SPacketTypeCallback.REGISTERED.invoker().accept(context.getPlayer(), ids);
	}

	@Override
	protected void onReceivedUnregisterPacket(PacketContext context, Collection<String> ids) {
		C2SPacketTypeCallback.UNREGISTERED.invoker().accept(context.getPlayer(), ids);
	}

	@Override
	public Packet<?> toPacket(String id, PacketByteBuf buf) {
		return new CustomPayloadS2CPacket(id, buf);
	}

	@Override
	public Packet<?> toPacket(PacketIdentifier id, PacketByteBuf buf) {
		return this.toPacket(id.toString(), buf);
	}
}
