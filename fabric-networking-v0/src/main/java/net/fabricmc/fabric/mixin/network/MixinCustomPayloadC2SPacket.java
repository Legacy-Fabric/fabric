package net.fabricmc.fabric.mixin.network;

import net.fabricmc.fabric.impl.network.CustomPayloadC2SPacketAccessor;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadC2SPacket.class)
public class MixinCustomPayloadC2SPacket implements CustomPayloadC2SPacketAccessor {

	@Shadow
	private String field_6403;

	@Shadow
	private PacketByteBuf field_6404;

	@Override
	public String getChannel() {
		return this.field_6403;
	}

	@Override
	public PacketByteBuf getData() {
		return this.field_6404;
	}
}
