package net.fabricmc.fabric.mixin.network;

import net.fabricmc.fabric.impl.network.CustomPayloadPacketAccessor;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadS2CPacket.class)
public class MixinCustomPayloadS2CPacket implements CustomPayloadPacketAccessor {

	@Shadow
	private String field_6157;

	@Shadow
	private PacketByteBuf field_6158;

	@Override
	public String getChannel() {
		return this.field_6157;
	}

	@Override
	public PacketByteBuf getData() {
		return this.field_6158;
	}
}
