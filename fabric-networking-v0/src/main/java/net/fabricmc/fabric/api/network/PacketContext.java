package net.fabricmc.fabric.api.network;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.player.PlayerEntity;

public interface PacketContext {
	EnvType getPacketEnvironment();
	
	PlayerEntity getPlayer();
}
