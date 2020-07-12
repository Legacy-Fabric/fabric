package net.fabricmc.fabric.mixin.blockrenderlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class MixinBlock {

	@Inject(method = "getRenderLayerType", at = @At("HEAD"),cancellable = true)
	public void setRenderLayer(CallbackInfoReturnable<RenderLayer> cir){
		if (BlockRenderLayerMapImpl.blockRenderLayerMap.containsKey(this)){
			cir.setReturnValue(BlockRenderLayerMapImpl.blockRenderLayerMap.get(this));
			cir.cancel();
		}
	}
}
