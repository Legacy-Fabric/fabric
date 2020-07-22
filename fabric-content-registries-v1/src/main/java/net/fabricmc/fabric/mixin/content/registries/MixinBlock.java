package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

import net.fabricmc.fabric.api.block.FabricBlockMaterial;
import net.fabricmc.fabric.impl.content.registries.FabricPickaxeItem;
import net.fabricmc.fabric.impl.content.registries.FabricShovelItem;

@Mixin(Block.class)
public class MixinBlock {
	@Inject(method = "<init>(Lnet/minecraft/block/Material;Lnet/minecraft/block/MaterialColor;)V", at = @At("RETURN"))
	public void injectMaterial(Material material, MaterialColor color, CallbackInfo info) {
		if(!(FabricPickaxeItem.BLOCKS_BY_MINING_LEVEL.containsKey((Block) (Object) this)) && (material == Material.STONE || material == Material.IRON || material == Material.IRON2)) {
			if(material instanceof FabricBlockMaterial) {
				FabricPickaxeItem.MAP_ADDER.accept((Block) (Object) this, ((FabricBlockMaterial) material).getMiningLevel());
			}
			else {
				FabricPickaxeItem.MAP_ADDER.accept((Block) (Object) this, 0);
			}
		}
		else if(!(FabricShovelItem.BLOCKS_BY_MINING_LEVEL.containsKey((Block) (Object) this)) && (material == Material.GRASS || material == Material.DIRT || material == Material.CLAY || material == Material.SNOW || material == Material.SNOW_LAYER || material == Material.NOTEBLOCK)){
			if(material instanceof FabricBlockMaterial) {
				FabricShovelItem.MAP_ADDER.accept((Block) (Object) this, ((FabricBlockMaterial) material).getMiningLevel());
			}
			else {
				FabricShovelItem.MAP_ADDER.accept((Block) (Object) this, 0);
			}
		}
	}
}
