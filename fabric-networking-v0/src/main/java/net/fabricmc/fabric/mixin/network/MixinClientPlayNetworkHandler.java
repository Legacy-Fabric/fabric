package net.fabricmc.fabric.mixin.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.impl.network.ClientSidePacketRegistryImpl;
import net.fabricmc.fabric.impl.network.CustomPayloadC2SPacketAccessor;
import net.fabricmc.fabric.impl.network.PacketRegistryImpl;
import net.fabricmc.fabric.impl.network.PacketTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements PacketContext {

	@Shadow
	private MinecraftClient client;

	@Shadow
	public abstract void sendPacket(Packet<?> packet);

	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	public void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo info) {
		if (((ClientSidePacketRegistryImpl) ClientSidePacketRegistry.INSTANCE).accept(((CustomPayloadC2SPacketAccessor)packet).getChannel(), this, packet::getData)) {
			info.cancel();
		}
	}

	@Inject(method = "onCustomPayload", at = @At(value = "CONSTANT", args = "stringValue=Unknown custom packed identifier: {}"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
	public void onCustomPayloadNotFound(CustomPayloadS2CPacket packet, CallbackInfo info, String id, PacketByteBuf buf) {
		if (((CustomPayloadC2SPacketAccessor)packet).getChannel().equals(PacketTypes.REGISTER) || ((CustomPayloadC2SPacketAccessor)packet).getChannel().equals(PacketTypes.UNREGISTER)) {
			if (buf.refCnt() > 0) {
				buf.release();
			}
			info.cancel();
		}
	}

	@Inject(at = @At("RETURN"), method = "onGameJoin")
	public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
		Optional<Packet<?>> optionalPacket = PacketRegistryImpl.createInitialRegisterPacket(ClientSidePacketRegistry.INSTANCE);

		optionalPacket.ifPresent(this::sendPacket);
	}

	@Override
	public EnvType getPacketEnvironment() {
		return EnvType.CLIENT;
	}

	@Override
	public PlayerEntity getPlayer() {
		return this.client.player;
	}
}
