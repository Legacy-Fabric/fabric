package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelProperties;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements RegistryRemapperAccess {
	@Unique
	private RegistryRemapper itemRemapper = new RegistryRemapper(Item.REGISTRY);
	@Unique
	private RegistryRemapper blockRemapper = new RegistryRemapper(Block.REGISTRY);

	@Override
	public RegistryRemapper getItemRemapper() {
		return itemRemapper;
	}

	@Override
	public RegistryRemapper getBlockRemapper() {
		return blockRemapper;
	}

	@Inject(at = @At("TAIL"), method = {"<init>()V", "<init>(Lnet/minecraft/world/level/LevelInfo;Ljava/lang/String;)V"})
	public void init(CallbackInfo ci) {
		this.itemRemapper.dump();
		this.blockRemapper.dump();
	}


	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V")
	public void init(CompoundTag worldNbt, CallbackInfo ci) {
		this.itemRemapper.fromTag(worldNbt.getCompound("FabricItemRegistry"));
		this.blockRemapper.fromTag(worldNbt.getCompound("FabricBlockRegistry"));
	}

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/LevelProperties;)V")
	public void init(LevelProperties worldNbt, CallbackInfo ci) {
		this.itemRemapper = ((RegistryRemapperAccess) worldNbt).getItemRemapper().copy();
		this.blockRemapper = ((RegistryRemapperAccess) worldNbt).getBlockRemapper().copy();
	}
}
