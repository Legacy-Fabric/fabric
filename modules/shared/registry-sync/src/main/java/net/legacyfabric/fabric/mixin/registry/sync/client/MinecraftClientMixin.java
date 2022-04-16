package net.legacyfabric.fabric.mixin.registry.sync.client;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements RegistryRemapperAccess {
	@Unique
	private final RegistryRemapper itemRemapper = new RegistryRemapper(Item.REGISTRY);
	@Unique
	private final RegistryRemapper blockRemapper = new RegistryRemapper(Block.REGISTRY);

	@Override
	public RegistryRemapper getItemRemapper() {
		return this.itemRemapper;
	}

	@Override
	public RegistryRemapper getBlockRemapper() {
		return this.blockRemapper;
	}
}
