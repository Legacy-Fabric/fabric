package net.legacyfabric.fabric.test.networking;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacyfabric.fabric.api.networking.v1.PacketByteBufs;
import net.legacyfabric.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkingTest implements ClientModInitializer, ModInitializer {
	private static final String PARTICLE_PACKET_ID = "FABRIC|ParticlePacket";

	@Override
	public void onInitializeClient() {
		KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("fabric:test_keybinding", Keyboard.KEY_R, "fabric.key.categories.test"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			if (keyBinding.wasPressed()) {
				client.player.sendChatMessage("Key was pressed!");
				ClientPlayNetworking.send(PARTICLE_PACKET_ID, PacketByteBufs.empty());
			}
		});
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(PARTICLE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			((ServerWorld) player.world).spawnParticles(ParticleTypes.VILLAGER_ANGRY, player.x, player.y + 1, player.z, 1, 0, 0.5, 0, 0.1);
		});
	}
}
