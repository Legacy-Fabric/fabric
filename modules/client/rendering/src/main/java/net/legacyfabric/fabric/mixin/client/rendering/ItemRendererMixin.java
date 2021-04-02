package net.legacyfabric.fabric.mixin.client.rendering;

import net.legacyfabric.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.legacyfabric.fabric.impl.client.rendering.BuiltinItemRendererRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Inject(method = "method_3982", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityItemStackRenderHelper;method_3349(Lnet/minecraft/item/ItemStack;)Ve", shift = At.Shift.AFTER))
	public void onRender(ItemStack stack, BakedModel bakedModel, CallbackInfo ci) {
		BuiltinItemRendererRegistry.DynamicItemRenderer renderer = BuiltinItemRendererRegistryImpl.getRenderer(stack.getItem());
		if (renderer != null) {
			renderer.render(stack, bakedModel.getTransformation());
		}
	}
}
