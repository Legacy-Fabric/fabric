package net.fabricmc.fabric.mixin.client.rendering;

import java.util.Map;

import com.google.common.collect.Maps;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
	@Shadow
	private Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> classMap;

	@Inject(method = "<init>", at = @At("RETURN"), require = 0)
	public void init(TextureManager textureManager, ItemRenderer itemRenderer, CallbackInfo info) {
		EntityRendererRegistry.INSTANCE.initialize((EntityRenderDispatcher) (Object) this, textureManager, itemRenderer, classMap);
	}
}
