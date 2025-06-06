/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.impl.registry;

import java.io.IOException;
import java.io.UncheckedIOException;

import net.minecraft.nbt.NbtCompound;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientRemapInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(RegistryHelperImplementation.PACKET_ID, (client, handler, buf, responseSender) -> {
			NbtCompound nbt;

			try {
				nbt = buf.readNbtCompound();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}

			client.submit(() -> RegistryHelperImplementation.readAndRemap(nbt));
		});
	}
}
