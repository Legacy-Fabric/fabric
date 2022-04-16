package net.legacyfabric.fabric.impl.client.registry.sync;

import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientRemapInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(RegistryRemapper.PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> ((RegistryRemapperAccess) client.world).remap());
		});
	}
}
