package net.fabricmc.fabric.mixin.client.rendering;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher {
	@Shadow
	private
	Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderers;

	@Inject(method = "<init>()V", at = @At("RETURN"))
	public void init(CallbackInfo info) {
		BlockEntityRendererRegistry.INSTANCE.initialize((BlockEntityRenderDispatcher) (Object) this, this.renderers);
	}
}
