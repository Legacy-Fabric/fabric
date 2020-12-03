/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.test.network;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketIdentifier;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;

@SuppressWarnings("unused")
public class NetworkingTest {
	public static void initialize() {
		ServerEntityEvents.LIGHTNING_STRIKE.register((entity, world, x, y, z) -> {
			Stream<PlayerEntity> playerStream = PlayerStream.watching(world, new BlockPos(x, y, z));
			playerStream.forEach((player) -> {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeBlockPos(new BlockPos(x, y, z));
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new PacketIdentifier("fabric-networking-v0-testmod", "s2c_test_packet"), buf, future -> {
					System.out.println("Completed!");
				});
			});
		});
		ServerSidePacketRegistry.INSTANCE.register(new PacketIdentifier("fabric-networking-v0-testmod", "c2s_test_packet"), (context, byteBuf) -> {
			String message = new String(byteBuf.readByteArray(), StandardCharsets.UTF_8);
			LogManager.getLogger().info(message);
		});
	}

	@Environment(EnvType.CLIENT)
	public static void clientInitialize() {
		ClientSidePacketRegistry.INSTANCE.register(new PacketIdentifier("fabric-networking-v0-testmod", "s2c_test_packet"), ((context, byteBuf) -> {
			BlockPos pos = byteBuf.readBlockPos();
			context.getTaskQueue().execute(() -> {
				MinecraftClient.getInstance().particleManager.addBlockBreakParticles(pos, Blocks.STONE.getDefaultState());
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeBytes("Hello from the Netty I/O Thread!".getBytes(StandardCharsets.UTF_8));
				ClientSidePacketRegistry.INSTANCE.sendToServer(new PacketIdentifier("fabric-networking-v0-testmod", "c2s_test_packet"), buf);
			});
		}));
	}
}
