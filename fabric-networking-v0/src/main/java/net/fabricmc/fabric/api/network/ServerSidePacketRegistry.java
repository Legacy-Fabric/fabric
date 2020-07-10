package net.fabricmc.fabric.api.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

public interface ServerSidePacketRegistry extends PacketRegistry {
	boolean canPlayerReceive(PlayerEntity player, String id);

	void sendToPlayer(PlayerEntity player, Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener);

	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		sendToPlayer(player, toPacket(id, buf), completionListener);
	}

	default void sendToPlayer(PlayerEntity player, Packet<?> packet) {
		sendToPlayer(player, packet, null);
	}

	default void sendToPlayer(PlayerEntity player, String id, PacketByteBuf buf) {
		sendToPlayer(player, id, buf, null);
	}
}
